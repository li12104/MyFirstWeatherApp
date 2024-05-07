package com.example.myfirstweatherapp.logic.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ServiceCreator {

    private const val BASE_URL="http://v1.yiketianqi.com/"

    private val retrofit=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    fun <T> create(serviceClass: Class<T>) : T= retrofit.create(serviceClass)
    inline fun<reified T> create() : T= create(T::class.java)
}