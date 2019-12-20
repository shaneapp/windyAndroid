package com.example.windyandroid.View.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.windyandroid.Data.City
import com.example.windyandroid.R
import kotlinx.android.synthetic.main.listitem_city_select.view.*

class CityAdapter(private val cityList: MutableList<City>, val cellClicked : (city : City) -> Unit) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.listitem_city_select, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cityList[position]
        holder.cityName.text = city.name
        holder.countryCode.text = city.country
        holder.llCell.setOnClickListener { cellClicked(city) }
    }

    override fun getItemCount(): Int {
        return cityList.count()
    }

    fun updateData(data: List<City>) {
        cityList.clear()
        cityList.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cityName = itemView.tvCityName
        var countryCode = itemView.tvCountryCode
        var llCell = itemView.llCitySelectCell
    }

}