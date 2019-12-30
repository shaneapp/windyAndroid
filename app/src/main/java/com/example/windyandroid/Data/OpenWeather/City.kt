package com.example.windyandroid.Data.OpenWeather

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Transient

@Entity
data class City(@Id var objectid: Long, var id: Long, var name: String, var country: String, @Transient var coord: Coord)