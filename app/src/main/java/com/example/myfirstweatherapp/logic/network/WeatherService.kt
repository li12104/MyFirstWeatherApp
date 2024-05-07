package com.example.myfirstweatherapp.logic.network

import com.example.myfirstweatherapp.WeatherApplication
import com.example.myfirstweatherapp.logic.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    /**
     * 通过城市名称获取天气信息
     */
    @GET("api?unescape=${WeatherApplication.UNESCAPE}&version=${WeatherApplication.VERSION}&appid=${WeatherApplication.APPID}&appsecret=${WeatherApplication.APPSECRET}")
    fun getWeathersWithCity(@Query("city") city: String): Call<WeatherResponse>
}