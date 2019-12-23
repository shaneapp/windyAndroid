package com.example.windyandroid.Model;

import android.app.Application
import android.content.Context
import com.example.windyandroid.Data.OpenWeather.City
import com.example.windyandroid.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CityModel(application: Application) {

    val cityList: List<City> by lazy {
        val cityType = object : TypeToken<List<City>>() {}.type
        Gson().fromJson<List<City>>(getCitiesFromFile(application), cityType)
    }

    private fun getCitiesFromFile(context: Context) : String {
        return context.resources.openRawResource(R.raw.city_list_minified).bufferedReader().use { it.readText() }
    }

}