package com.example.windyandroid.Model;

import android.app.Application
import com.example.windyandroid.Data.OpenWeather.City
import com.example.windyandroid.Data.OpenWeather.City_
import com.example.windyandroid.ObjectBox
import io.objectbox.Box
import io.objectbox.kotlin.boxFor

class CityModel(application: Application) {

    val cityBox: Box<City> = ObjectBox.boxStore.boxFor()

    fun searchCity(searchTerm: String): List<City> {
        return cityBox.query().startsWith(City_.name, searchTerm).build().find()
    }

}