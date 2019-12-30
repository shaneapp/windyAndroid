package com.example.windyandroid

import android.app.Application

class WindyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        ObjectBox.init(this)
    }
}
