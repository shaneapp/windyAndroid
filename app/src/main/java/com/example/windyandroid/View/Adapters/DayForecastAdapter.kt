package com.example.windyandroid.View.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.utils.Utils
import com.bumptech.glide.Glide
import com.example.windyandroid.Data.OpenWeather.City
import com.example.windyandroid.Data.OpenWeather.ForecastData
import com.example.windyandroid.Data.OpenWeather.WeatherData
import com.example.windyandroid.R
import com.example.windyandroid.kelvinToCelsius
import com.example.windyandroid.millisToSeconds
import kotlinx.android.synthetic.main.listitem_city_select.view.*
import kotlinx.android.synthetic.main.listitem_day_forecast.view.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

class DayForecastAdapter(private val context: Context, private val todaysForecast: MutableList<WeatherData>) : RecyclerView.Adapter<DayForecastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.listitem_day_forecast, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecastItem = todaysForecast[position]
        val forecastDate = DateTime(millisToSeconds(forecastItem.dt))
        holder.tvForecastTime.text = SimpleDateFormat("HH:mm").format(forecastDate.toDate())
        Glide.with(context).load("https://openweathermap.org/img/wn/${forecastItem.weather[0].icon}@2x.png").into(holder.ivForecastIcon)
        holder.tvForecastTemp.text = "${kelvinToCelsius(forecastItem.main.temp).roundToInt()}\u00b0"
    }

    override fun getItemCount(): Int {
        return todaysForecast.count()
    }

    fun updateData(data: List<WeatherData>) {
        todaysForecast.clear()
        todaysForecast.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvForecastTime = itemView.tvForecastTime
        var ivForecastIcon = itemView.ivForecastIcon
        var tvForecastTemp = itemView.tvForecastTemp
    }

}