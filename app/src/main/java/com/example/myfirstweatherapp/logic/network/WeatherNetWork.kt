package com.example.myfirstweatherapp.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object WeatherNetWork {

    private val weatherService =ServiceCreator.create<WeatherService>()

    suspend fun getWeather(city: String)= weatherService.getWeathersWithCity(city).await()

    private suspend fun <T> Call<T>.await() :T{
        return suspendCoroutine {
            enqueue(object : Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful){
                        val body=response.body()
                        if (body!=null) it.resume(body)
                        else it.resumeWithException(
                            RuntimeException("返回数据为空")
                        )
                    }
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    it.resumeWithException(t)
                }
            })
        }
    }

}