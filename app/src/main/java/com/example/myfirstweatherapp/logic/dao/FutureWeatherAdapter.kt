package com.example.myfirstweatherapp.logic.dao

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstweatherapp.R
import com.example.myfirstweatherapp.logic.model.HourlyWeather

class FutureWeatherAdapter(val hours: List<HourlyWeather>) :
    RecyclerView.Adapter<FutureWeatherAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeText = view.findViewById<TextView>(R.id.futureTime)
        val futTmp = view.findViewById<TextView>(R.id.futureTmp)
        val futWea = view.findViewById<TextView>(R.id.futureWeather)
        val futAqi = view.findViewById<TextView>(R.id.futureAqi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.future_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = hours.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weaFut=hours[position]
        holder.timeText.text=weaFut.hours
        holder.futTmp.text=weaFut.tem+"℃"
        holder.futWea.text=weaFut.wea
        holder.futAqi.text=weaFut.aqi
    }
}