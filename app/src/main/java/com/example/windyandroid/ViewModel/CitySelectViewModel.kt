package com.example.windyandroid.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.windyandroid.Data.City
import com.example.windyandroid.Data.Unsplash.Photo
import com.example.windyandroid.Model.CityModel
import com.example.windyandroid.NetworkApi
import com.example.windyandroid.TempStore
import io.reactivex.Observable

class CitySelectViewModel constructor(application: Application) : AndroidViewModel(application) {

    val cityModel = CityModel(application)

    fun getAllCities() : Observable<List<City>> {
        return Observable.just(cityModel.cityList)
    }

    fun getFilteredCityListByName(cityFilter: String) : Observable<List<City>> {
        return Observable.just(cityModel.cityList.filter { it.name.contains(cityFilter, true) })
    }

    fun fetchCityImageFromUnsplash(cityName: String) : Observable<Photo> {
        return NetworkApi.api.getImagesList(NetworkApi.UNSPLASH_ACCESS_KEY, cityName)
    }

    fun setCurrentCity(city: City) {
        TempStore.currentCity = city
    }

}