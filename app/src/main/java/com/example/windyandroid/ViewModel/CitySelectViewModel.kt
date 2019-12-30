package com.example.windyandroid.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.windyandroid.Data.OpenWeather.City
import com.example.windyandroid.Model.CityModel
import com.example.windyandroid.NetworkApi
import com.example.windyandroid.CurrentCityData
import com.example.windyandroid.TempStore
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class CitySelectViewModel constructor(application: Application) : AndroidViewModel(application) {

    val cityModel = CityModel(application)

    fun getFilteredCityListByName(cityFilter: String) : Observable<List<City>> {
        return Observable.just(cityModel.searchCity(cityFilter))
    }

    fun fetchAllDataForCity(city: City): Observable<CurrentCityData> {
        return Observable.zip(NetworkApi.unsplashApi.getImagesList(NetworkApi.UNSPLASH_ACCESS_KEY, "${city.name} city", true),
            NetworkApi.openweatherApi.getCurrentWeather(city.id, NetworkApi.OPENWEATHER_API_KEY),
            BiFunction { photo, weather -> CurrentCityData(city, photo, weather) })
    }

    fun setCurrentCityData(tempCity: CurrentCityData) {
        TempStore.currentCity = tempCity
    }

}