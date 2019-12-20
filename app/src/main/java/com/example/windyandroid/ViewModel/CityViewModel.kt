package com.example.windyandroid.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.windyandroid.Data.City
import com.example.windyandroid.Model.CityModel
import io.reactivex.Observable

class CityViewModel constructor(application: Application) : AndroidViewModel(application) {

    val cityModel = CityModel(application)

    fun getAllCities() : Observable<List<City>> {
        return Observable.just(cityModel.cityList)
    }

    fun getFilteredCityListByName(cityFilter: String) : Observable<List<City>> {
        return Observable.just(cityModel.cityList.filter { it.name.contains(cityFilter, true) })
    }

}