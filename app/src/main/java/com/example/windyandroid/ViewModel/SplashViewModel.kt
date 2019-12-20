package com.example.windyandroid.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.windyandroid.Data.City
import com.example.windyandroid.Model.CityModel
import io.reactivex.Observable

class SplashViewModel constructor(application: Application) : AndroidViewModel(application) {

    val cityModel = CityModel(application)

    fun getCityList() : Observable<List<City>> {
        return Observable.just(cityModel.cityList)
    }

}