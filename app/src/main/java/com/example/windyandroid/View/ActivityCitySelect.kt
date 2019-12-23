package com.example.windyandroid.View

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windyandroid.Data.OpenWeather.City
import com.example.windyandroid.R
import com.example.windyandroid.TempCity
import com.example.windyandroid.View.Adapters.CityAdapter
import com.example.windyandroid.ViewModel.CitySelectViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_city_select.*
import kotlinx.android.synthetic.main.toolbar_search.*

class ActivityCitySelect : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var subscriptionCityFilter: Disposable

    // TODO: use same viewmodel as ActivitySplash because is accesses the CityModel?
    private lateinit var viewModel: CitySelectViewModel

    private lateinit var cityAdapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_select)

        viewModel = ViewModelProviders.of(this).get(CitySelectViewModel::class.java)

        // city recycler view
        rvCityResults.layoutManager = LinearLayoutManager(this)
        cityAdapter = CityAdapter(mutableListOf()) { city: City ->
            Toast.makeText(this@ActivityCitySelect, "${city.name} selected", Toast.LENGTH_SHORT).show()
            compositeDisposable.add(
                viewModel.fetchAllDataForCity(city)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this@ActivityCitySelect::cityDataLoaded) {
                        it.printStackTrace()
                    }
            )
        }
        rvCityResults.adapter = cityAdapter

        // search field
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s.let {
                    if (!it.isNullOrEmpty()) {
                        rvCityResults.visibility = View.VISIBLE

                        subscriptionCityFilter = viewModel.getFilteredCityListByName(it.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this@ActivityCitySelect::updateCityList)

                        // TODO: does this leak subscriptions?
                        compositeDisposable.add(subscriptionCityFilter)

                    } else {
                        rvCityResults.visibility = View.INVISIBLE
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        ivSearchCross.setOnClickListener {
            etSearch.text.clear()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        compositeDisposable.clear()
        super.onPause()
    }

    fun updateCityList(cityList: List<City> ) {
        cityAdapter.updateData(cityList)
    }

    fun cityDataLoaded(tempCity: TempCity) {
        viewModel.setCurrentCityData(tempCity)
    }

}