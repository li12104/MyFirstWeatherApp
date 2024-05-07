package com.example.myfirstweatherapp.logic.model

data class Province(var province: String,var leaders: List<Leader>) {
}

data class Leader(var leader: String,var cities: List<City>)

data class City(var city: String)

data class CityTmp(var p: String, var l: String, var c: String)

