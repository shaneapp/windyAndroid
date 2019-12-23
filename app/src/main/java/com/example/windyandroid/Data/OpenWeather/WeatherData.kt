package com.example.windyandroid.Data.OpenWeather

data class WeatherData(val coord: Coord, val weather: List<Weather>, val base: String, val main: Main, val visibility: Int, val wind: Wind, val clouds: Clouds, val dt: Long)