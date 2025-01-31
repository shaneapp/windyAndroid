package com.example.windyandroid.View

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windyandroid.Data.OpenWeather.City
import com.example.windyandroid.ObjectBox
import com.example.windyandroid.R
import com.example.windyandroid.View.Adapters.CityAdapter
import com.example.windyandroid.ViewModel.CitySelectViewModel
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_city_select.*
import kotlinx.android.synthetic.main.toolbar_search.*

class ActivityCitySelect : BaseActivity() {

    // TODO: use same viewmodel as ActivitySplash because is accesses the CityModel?
    private lateinit var viewModel: CitySelectViewModel
    private lateinit var cityAdapter: CityAdapter

    private val cityBox: Box<City> = ObjectBox.boxStore.boxFor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_select)

        viewModel = ViewModelProviders.of(this).get(CitySelectViewModel::class.java)

        setupCityListRecyclerView()
        setupSearchField()

        ivSearchCross.setOnClickListener {
            etSearch.text.clear()
        }
    }

    private fun setupCityListRecyclerView() {
        rvCityResults.layoutManager = LinearLayoutManager(this)
        cityAdapter = CityAdapter(mutableListOf()) { city: City ->
            showLoadingDialog()
            requestAllDataForCity(city)
        }
        rvCityResults.adapter = cityAdapter
    }

    private fun setupSearchField() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(searchText: CharSequence?, start: Int, before: Int, count: Int) {
                if (!searchText.isNullOrEmpty()) {
                    rvCityResults.visibility = View.VISIBLE
                    updateCityListWithFilter(searchText.toString())
                } else {
                    rvCityResults.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun updateCityListWithFilter(searchText: String) {
        // TODO: does this leak subscriptions because we don't unsubscribe per text change?
        viewModel.getFilteredCityListByName(searchText)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    cityBox.put(it)
                    cityAdapter.updateData(it)
                },
                onError = { it.printStackTrace() }
            )
            .addTo(compositeDisposable)
    }

    private fun requestAllDataForCity(city: City) {
        viewModel.fetchAllDataForCity(city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    viewModel.setCurrentCityData(it)
                    hideLoadingDialog()
                    startActivity(Intent(this, ActivityWeather::class.java))
                },
                onError = {
                    it.printStackTrace()
                    hideLoadingDialog()
                }
            )
            .addTo(compositeDisposable)
    }

}