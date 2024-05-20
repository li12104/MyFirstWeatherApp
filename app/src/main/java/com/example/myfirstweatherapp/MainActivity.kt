package com.example.myfirstweatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.example.myfirstweatherapp.WeatherApplication.Companion.showToast
import com.example.myfirstweatherapp.databinding.ActivityMainBinding
import com.example.myfirstweatherapp.logic.model.WeatherResponse
import com.example.myfirstweatherapp.ui.weather.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.lang.RuntimeException
import java.util.Locale
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: WeatherViewModel

    lateinit var weather: WeatherResponse

    private lateinit var pvOptions: OptionsPickerView<String>

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient//位置信息

    private lateinit var locationManager: LocationManager

    private lateinit var fragment: WeatherFragment

    var chooseCity = ""

    private var longitude = 0.0
    private var latitude = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            // 使用 ViewModelProvider 创建 ViewModel 实例
            viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
            // 观察 weather LiveData
            viewModel.weather.observe(this) {
                if (it != null) {
                    weather = it
                    // 更新 UI
                    fragment = WeatherFragment(it)
                    binding.weatherNow.weatherNowConditionTextView.text = it.wea
                    binding.weatherNow.temperatureNowTextView.text = it.tem + "°C"
                    binding.weatherNow.airLevel.text = it.air_level
                    binding.weatherNow.advice.text = it.air_tips
                    pageBg()
                }
            }
            viewModel.chooseCity.observe(this) {
                if (it != null) {
                    viewModel.fetchWeather(it)
                    binding.cityName.text = it
                    putLocalCity(it)
                }
            }
            initPickerView()//初始化pickerview
            binding.changeCity.setOnClickListener(this)
            chooseCity = getLocalCity()
            if (chooseCity != "") {
                viewModel.changeCity(chooseCity)
            }
            binding.menu.setOnClickListener(this)
            binding.upFresh.setOnRefreshListener {
                freshWeather()
            }
        } catch (e: Exception) {
            "出了点小问题".showToast()
        }
    }

    private fun freshWeather(){
        thread {
            Thread.sleep(2000)
            runOnUiThread{
                viewModel.fetchWeather(chooseCity)
                "刷新成功".showToast()
                binding.upFresh.isRefreshing=false
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.changeCity -> {
                pvOptions.show()
            }

            R.id.menu -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.weatherInfo, fragment)
                    .commit()
                binding.root.openDrawer(GravityCompat.START)
            }
        }
    }


    fun pageBg() {
        if (weather.wea.contains("晴")) {
            binding.weatherContent.background =
                ContextCompat.getDrawable(this, R.drawable.bg_clear_day)
        } else if (weather.wea.contains("雨")) {
            binding.weatherContent.background = ContextCompat.getDrawable(this, R.drawable.bg_rain)
        } else if (weather.wea.contains("云") || weather.wea.contains("阴")) {
            binding.weatherContent.background =
                ContextCompat.getDrawable(this, R.drawable.bg_cloudy)
        } else if (weather.wea.contains("雪")) {
            binding.weatherContent.background = ContextCompat.getDrawable(this, R.drawable.bg_snow)
        } else if (weather.wea.contains("风")) {
            binding.weatherContent.background = ContextCompat.getDrawable(this, R.drawable.bg_wind)
        } else {
            binding.weatherContent.background =
                ContextCompat.getDrawable(this, R.drawable.bg_partly_cloudy_day)
        }
    }

    override fun onResume() {
        super.onResume()
        //判断是否具有定位权限
        initLocalInfo()
    }


    fun getLocalCity(): String {
        val s = getSharedPreferences("city", 0)
        return s.getString("localCity", "").toString()
    }

    fun putLocalCity(s: String) {
        getSharedPreferences("city", 0)
            .edit()
            .putString("localCity", s)
            .apply()
    }

    //初始化pickerView
    private fun initPickerView() {
        val provinces = WeatherApplication.provinces
        // 省份列表
        val provinceList = provinces.map { it.province }
        // 领导列表（初始为空，稍后会填充）
        val leaderLists = mutableListOf<MutableList<String>>()
        // 城市列表（初始为空，稍后会填充）
        val cityLists = mutableListOf<MutableList<MutableList<String>>>()
        // 遍历省份列表，为每个省份构建领导和城市的列表
        provinces.forEach { province ->
            val leaders = mutableListOf<String>()
            val cities = mutableListOf<MutableList<String>>()
            province.leaders.forEach { leader ->
                leaders.add(leader.leader)
                val cityList = mutableListOf<String>()
                leader.cities.forEach { city ->
                    cityList.add(city.city)
                }
                cities.add(cityList)
            }
            leaderLists.add(leaders)
            cityLists.add(cities)
        }
        // 创建 OptionsPickerBuilder 实例
        pvOptions = OptionsPickerBuilder(
            this
        ) { options1, options2, options3, _ ->
            val city = cityLists[options1][options2][options3]
            // 使用选中的省份、领导和城市做进一步处理
            chooseCity=city
            viewModel.changeCity(city)
        }.setTitleText("城市选择")
            .build()
        // 设置选择器数据
        pvOptions.setPicker(provinceList, leaderLists, cityLists)
    }

    private fun initLocalInfo() {
        //用户是否给予了定位权限
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            //用户是否开启了定位
            if (isLocationEnabled(this)) {
                //开启了定位
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener(this) { location: Location? ->
                        if (location != null) {
                            // 使用获取到的位置信息，例如经纬度等
                            latitude = location.latitude
                            longitude = location.longitude
                            // 你可以在这里调用反向地理编码服务，如上面提到的Google Maps Geocoding API，来获取城市信息。
                            println(latitude)
                            println(longitude)
                        } else {
                            // 无法获取位置信息时的处理逻辑
                            "未获取到经纬度信息".showToast()
                        }
                    }
            } else {
                promptForEnableLocation(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // 权限被授予
                if (isLocationEnabled(this)) {
                    initLocalInfo()
                } else {
                    promptForEnableLocation(this)
                }
            } else {
                // 权限被拒绝，再次申请
                AlertDialog.Builder(this)
                    .setTitle("必要权限")
                    .setMessage("获取当前城市天气必须拥有定位权限")
                    .setCancelable(false)
                    .setPositiveButton(
                        "确定"
                    ) { _, _ ->
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                            1
                        )
                    }
                    .create().show()

            }
        }
    }


    //检查是否打开了定位
    fun isLocationEnabled(context: Context): Boolean {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    //用户未打开定位，提示开启定位
    fun promptForEnableLocation(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("定位服务未开启")
            .setMessage("为了应用能够正常工作，需要开启定位服务。是否现在去设置？")
            .setPositiveButton("去设置") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }
            .setNegativeButton("取消", null)
            .show()
    }


}