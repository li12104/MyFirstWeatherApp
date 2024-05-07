package com.example.myfirstweatherapp.ui.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstweatherapp.WeatherApplication.Companion.showToast
import com.example.myfirstweatherapp.logic.model.WeatherResponse
import com.example.myfirstweatherapp.logic.network.WeatherNetWork
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    // 使用 LiveData 来包装 WeatherResponse 对象
    private val _weather = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> get() = _weather

    // 请求错误
//    private val _error = MutableLiveData<Exception?>()
//    val error: LiveData<Exception?> get() = _error

    private val _chooseCity=MutableLiveData<String>()

    val chooseCity:LiveData<String> get()=_chooseCity

    // 请求状态，用于展示加载指示器
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    // 使用协程发起网络请求并更新数据
    fun fetchWeather(cityName: String) {
        // 如果已经在加载，则不发起新的请求
        if (_isLoading.value == true) return
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val weatherResponse = WeatherNetWork.getWeather(cityName)
                _weather.value = weatherResponse
//                clearError() // 清除之前的错误状态
            } catch (e: Exception) {
                Log.e("e", "$e")
                "网络不给力，请重启再试".showToast()
//                _error.value = e // 传递错误给观察者
            } finally {
                _isLoading.value = false // 请求结束，设置加载状态为 false
            }
        }
    }
    // 清除错误状态
//    fun clearError() {
//        _error.value = null
//    }

    fun changeCity(cityName: String){
        _chooseCity.value=cityName
    }


}