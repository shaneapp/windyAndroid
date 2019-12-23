package com.example.windyandroid

import com.example.windyandroid.Data.OpenWeather.City
import com.example.windyandroid.Data.OpenWeather.WeatherData
import com.example.windyandroid.Data.Unsplash.Photo

data class TempCity(val city: City, val photo: Photo, val weather: WeatherData)