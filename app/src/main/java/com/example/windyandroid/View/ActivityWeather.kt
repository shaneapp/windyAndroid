package com.example.windyandroid.View

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.windyandroid.R
import com.example.windyandroid.ViewModel.WeatherViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_weather.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import kotlin.math.roundToInt

class ActivityWeather : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        compositeDisposable.addAll(
            viewModel.getCityData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it != null) {
                        Glide.with(this@ActivityWeather).load(it.photo.urls.regular).into(ivCityImage)
                        tvCityName.text = it.city.name
                        tvWeatherDay.text = "${it.weather.weather[0].main}, ${getDayOfWeek()}"
                        tvCurrentTemp.text = "${kelvinToCelsius(it.weather.main.temp).roundToInt()}\u00b0"
                    }
                }, {
                    it.printStackTrace()
                })

        )

    }

    override fun onResume() {
        super.onResume()

        compositeDisposable.add(
            viewModel.get5DayForecast()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it != null) {
                        Toast.makeText(this@ActivityWeather, it.list.count().toString(), Toast.LENGTH_SHORT).show()
                    }
                }, {
                    it.printStackTrace()
                })
        )
    }

    override fun onPause() {
        compositeDisposable.clear()
        super.onPause()
    }

    fun getDayOfWeek(): String {
        return DateTimeFormat.forPattern("EEEE").print(DateTime())
    }

    fun kelvinToCelsius(kelvin: Double): Double {
        return "%.1f".format(kelvin - 273.15f).toDouble()
    }

}