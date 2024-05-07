package com.example.myfirstweatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstweatherapp.WeatherApplication.Companion.showToast
import com.example.myfirstweatherapp.databinding.FragWeatherBinding
import com.example.myfirstweatherapp.logic.dao.FutureWeatherAdapter
import com.example.myfirstweatherapp.logic.model.WeatherResponse
import com.example.myfirstweatherapp.ui.weather.WeatherViewModel
import com.google.gson.Gson

class WeatherFragment(val weatherResponse: WeatherResponse) : Fragment() {

    private lateinit var binding: FragWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            binding = FragWeatherBinding.inflate(layoutInflater)
            binding.futureRecycleView.layoutManager = LinearLayoutManager(context)
            binding.futureRecycleView.adapter = FutureWeatherAdapter(weatherResponse.hours)
            binding.apiLayout.kouzhaoText.text=weatherResponse.aqi.kouzhao
            binding.apiLayout.yundongText.text=weatherResponse.aqi.yundong
            binding.apiLayout.waichuText.text=weatherResponse.aqi.waichu
            binding.apiLayout.kaichuangText.text=weatherResponse.aqi.kaichuang

        }catch (e: Exception){
            "出了点小问题".showToast()
        }
        return binding.root
    }


}