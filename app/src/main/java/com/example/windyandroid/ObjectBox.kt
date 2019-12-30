package com.example.windyandroid

import android.content.Context
import com.example.windyandroid.Data.OpenWeather.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.Factory
import java.io.InputStream

object ObjectBox {

    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
            .initialDbFile{ context.resources.assets.open("data.mdb") }
            .androidContext(context.applicationContext)
            .build()
    }

}