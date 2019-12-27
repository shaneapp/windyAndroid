package com.example.windyandroid

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

fun kelvinToCelsius(kelvin: Double): Double {
    return "%.1f".format(kelvin - 273.15f).toDouble()
}

fun millisToSeconds(millis: Long): Long {
    return millis * 1000
}

fun getDayOfWeek(): String {
    return DateTimeFormat.forPattern("EEEE").print(DateTime())
}