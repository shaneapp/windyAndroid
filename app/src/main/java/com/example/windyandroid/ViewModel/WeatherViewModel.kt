package com.example.windyandroid.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.windyandroid.Data.OpenWeather.ForecastData
import com.example.windyandroid.NetworkApi
import com.example.windyandroid.CurrentCityData
import com.example.windyandroid.TempStore
import io.reactivex.Observable

class WeatherViewModel constructor(application: Application) : AndroidViewModel(application) {

    fun getCityData(): Observable<CurrentCityData?> {
        return Observable.just(TempStore.currentCity)
    }

    fun get5DayForecast(): Observable<ForecastData> {
        return NetworkApi.openweatherApi.getHourly4DayForecast(TempStore.currentCity!!.city.id, NetworkApi.OPENWEATHER_API_KEY)
    }

}