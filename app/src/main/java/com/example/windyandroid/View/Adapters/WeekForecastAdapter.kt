package com.example.windyandroid.View.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.windyandroid.Data.OpenWeather.WeatherData
import com.example.windyandroid.R
import com.example.windyandroid.kelvinToCelsius
import kotlinx.android.synthetic.main.listitem_day_forecast.view.ivForecastIcon
import kotlinx.android.synthetic.main.listitem_week_forecast.view.*
import org.joda.time.DateTime
import org.joda.time.LocalDate
import kotlin.math.roundToInt

class WeekForecastAdapter(private val context: Context, private val weekForecast: MutableMap<LocalDate, List<WeatherData>>) : RecyclerView.Adapter<WeekForecastAdapter.ViewHolder>() {

    // TODO: weekForecast must be in order for this to work
    init {
        weekForecast.toList().sortedBy { it.first }.toMap()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.listitem_week_forecast, parent, false))
    }


    // TODO: this is not actually a forecast for the days ahead, because this returns a list of 3 hourly forecasts and we only pick the first from the list here and even then, the API returns deviations from current temp, not an actual forecast min and max temp
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // if datetime in list equals the same date as today+position
        val dayOfWeek = LocalDate().plusDays(position)
        val forecastDayForPosition = weekForecast[dayOfWeek]
        if (forecastDayForPosition != null) {
            val forecastWeather = forecastDayForPosition[1]
            holder.tvForecastDay.text = dayOfWeek.dayOfWeek().asText
            Glide.with(context).load("https://openweathermap.org/img/wn/${forecastWeather.weather[0].icon}@2x.png").into(holder.ivForecastIcon)
            holder.tvForecastMin.text = "${kelvinToCelsius(forecastWeather.main.temp_min).roundToInt()}\u00b0"
            holder.tvForecastMax.text = "${kelvinToCelsius(forecastWeather.main.temp_max).roundToInt()}\u00b0"
        }
    }

    override fun getItemCount(): Int {
        return weekForecast.count()
    }

    fun updateData(data: Map<LocalDate, List<WeatherData>>) {
        weekForecast.clear()
        weekForecast.putAll(data)
        weekForecast.toList().sortedBy { it.first }.toMap()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvForecastDay = itemView.tvForecastDay
        var ivForecastIcon = itemView.ivForecastIcon
        var tvForecastMin = itemView.tvForecastMin
        var tvForecastMax = itemView.tvForecastMax
    }

}