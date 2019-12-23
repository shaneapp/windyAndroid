package com.example.windyandroid.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.windyandroid.TempCity
import com.example.windyandroid.TempStore
import io.reactivex.Observable

class WeatherViewModel constructor(application: Application) : AndroidViewModel(application) {

    fun getCityData(): Observable<TempCity?> {
        return Observable.just(TempStore.currentCity);
    }

}