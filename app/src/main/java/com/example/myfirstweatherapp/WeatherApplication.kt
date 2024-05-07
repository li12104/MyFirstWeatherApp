package com.example.myfirstweatherapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Toast
import com.example.myfirstweatherapp.logic.model.City
import com.example.myfirstweatherapp.logic.model.CityTmp
import com.example.myfirstweatherapp.logic.model.Leader
import com.example.myfirstweatherapp.logic.model.Province
import org.json.JSONArray
import org.json.JSONException
import java.io.InputStream

class WeatherApplication : Application() {//定义一个单例类用于存放全局的context

    @SuppressLint("StaticFieldLeak")//注解表示这是静态的
    companion object {
        lateinit var context: Context
        val cs=ArrayList<CityTmp>()
        lateinit var provinces:ArrayList<Province>
//        val city=ArrayList<City>()
//        var leaders= ArrayList<Leader>()
//        var provinces=ArrayList<Province>()
//        const val TOKEN="iIHNLbhUjbs0iYQn"//彩云天气api的token
        /**
         * 天气api所需要的id
         */
        const val APPID = "95947464"
        const val APPSECRET = "4DQOSCoZ"
        const val UNESCAPE = 1//如果您希望json不被unicode, 直接输出中文, 请传此参数: 1
        const val VERSION = "v63"//固定值: v63 每个接口的version值都不一样

        fun String.showToast(time: Int = Toast.LENGTH_SHORT) {
            Toast.makeText(context, this, time).show()
        }

        fun Int.showToast(time: Int = Toast.LENGTH_SHORT) {
            Toast.makeText(context, this, time).show()
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext//在创建的时候，初始化
        // 从res/raw读取JSON文件
        val inputStream: InputStream = resources.openRawResource(R.raw.citys)
        val reader = inputStream.bufferedReader()
        val jsonString = reader.use { it.readText() }
        inputStream.close()
        // 解析JSON字符串
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val cityObject = jsonArray.getJSONObject(i)
                val province = cityObject.getString("provinceZh")
                val leader = cityObject.getString("leaderZh")
                val city = cityObject.getString("cityZh")
                // 处理城市数据
                cs.add(CityTmp(province,leader,city))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        provinces=convertCityTmpListToProvinceList(cs)
    }
    private fun convertCityTmpListToProvinceList(cityTmpList: List<CityTmp>) :ArrayList<Province>{
        val provinceList = cityTmpList.groupBy { it.p }.map { (provinceName, cities) ->
            val leaders = cities.groupBy { it.l }.map { (leaderName, cities) ->
                Leader(leaderName, cities.map { City(it.c) })
            }
            Province(provinceName, leaders)
        }
        return provinceList as ArrayList<Province>
    }

}

