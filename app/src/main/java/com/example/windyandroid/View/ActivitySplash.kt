package com.example.windyandroid.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.windyandroid.Data.City
import com.example.windyandroid.R
import com.example.windyandroid.ViewModel.SplashViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*

class ActivitySplash : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)

        compositeDisposable.add(
            viewModel.getCityList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showCities)
        )
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        compositeDisposable.clear()
        super.onPause()
    }

    fun showCities(cities: List<City> ) {
        splashTitle.text = cities.count().toString()
    }

}