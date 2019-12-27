package com.example.windyandroid.Data.OpenWeather

data class ForecastData(val cod: String, val message: String, val list: List<WeatherData>, val city: City, val country: String, val population: Long, val timezone: Long, val sunrise: Long, val sunset: Long)