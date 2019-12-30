package com.example.windyandroid.View

import android.content.Intent
import android.os.Bundle
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.windyandroid.Data.OpenWeather.WeatherData
import com.example.windyandroid.R
import com.example.windyandroid.View.Adapters.DayForecastAdapter
import com.example.windyandroid.View.Adapters.WeekForecastAdapter
import com.example.windyandroid.ViewModel.WeatherViewModel
import com.example.windyandroid.getDayOfWeek
import com.example.windyandroid.kelvinToCelsius
import com.example.windyandroid.millisToSeconds
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

    private lateinit var todayForecastAdapter: DayForecastAdapter
    private lateinit var weekForecastAdapter: WeekForecastAdapter

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

        ivSearchButton.setOnClickListener {
            startActivity(Intent(this@ActivityWeather, ActivityCitySelect::class.java))
        }

        todayForecastAdapter = DayForecastAdapter(this, mutableListOf())

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        linearLayoutManager.stackFromEnd = true
        rvTodayForecast.layoutManager = linearLayoutManager
        rvTodayForecast.adapter = todayForecastAdapter

        weekForecastAdapter = WeekForecastAdapter(this, mutableMapOf())

        val linearLayoutManagerWeek = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvWeeklyForecast.layoutManager = linearLayoutManagerWeek
        rvWeeklyForecast.adapter = weekForecastAdapter

    }

    override fun onResume() {
        super.onResume()

        compositeDisposable.add(
            viewModel.get5DayForecast()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ forecastData ->
                    if (forecastData != null) {
                        val todaysForecast = forecastData.list.filter {
                            DateTime.now().toLocalDate() == DateTime(
                            millisToSeconds(it.dt)).toLocalDate()
                        }
                        todayForecastAdapter.updateData(todaysForecast)
                        rvTodayForecast.scrollToPosition(0)

                        val dayForecasts = forecastData.list.groupBy { DateTime(millisToSeconds(it.dt)).toLocalDate() }
                        weekForecastAdapter.updateData(dayForecasts)
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

}