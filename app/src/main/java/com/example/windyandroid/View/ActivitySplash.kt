package com.example.windyandroid.View

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.windyandroid.R
import com.example.windyandroid.ViewModel.CitySelectViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ActivitySplash : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var viewModel: CitySelectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = ViewModelProviders.of(this).get(CitySelectViewModel::class.java)

        compositeDisposable.add(
            viewModel.getAllCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    startActivity(Intent(this@ActivitySplash, ActivityCitySelect::class.java))
                }
        )
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        compositeDisposable.clear()
        super.onPause()
    }

}