package com.example.windyandroid.View

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windyandroid.Data.OpenWeather.City
import com.example.windyandroid.ObjectBox
import com.example.windyandroid.R
import com.example.windyandroid.CurrentCityData
import com.example.windyandroid.View.Adapters.CityAdapter
import com.example.windyandroid.ViewModel.CitySelectViewModel
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_city_select.*
import kotlinx.android.synthetic.main.toolbar_search.*

class ActivityCitySelect : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    // TODO: use same viewmodel as ActivitySplash because is accesses the CityModel?
    private lateinit var viewModel: CitySelectViewModel
    private lateinit var cityAdapter: CityAdapter
    private lateinit var dialogLoading: Dialog

    private val cityBox: Box<City> = ObjectBox.boxStore.boxFor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_select)

        viewModel = ViewModelProviders.of(this).get(CitySelectViewModel::class.java)

        setupLoadingDialog()
        setupCityListRecyclerView()
        setupSearchField()

        ivSearchCross.setOnClickListener {
            etSearch.text.clear()
        }
    }

    private fun setupLoadingDialog() {
        dialogLoading = Dialog(this)
        dialogLoading.setContentView(R.layout.dialog_progress)
        dialogLoading.setCancelable(false)
    }

    private fun setupCityListRecyclerView() {
        rvCityResults.layoutManager = LinearLayoutManager(this)
        cityAdapter = CityAdapter(mutableListOf()) { city: City ->
            dialogLoading.show()
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
            .subscribe(this@ActivityCitySelect::updateCityList)
            .addTo(compositeDisposable)
    }

    private fun requestAllDataForCity(city: City) {
        viewModel.fetchAllDataForCity(city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this@ActivityCitySelect::cityDataLoaded) {
                it.printStackTrace()
                dialogLoading.hide()
            }
            .addTo(compositeDisposable)
    }

    override fun onPause() {
        compositeDisposable.clear()
        super.onPause()
    }

    fun updateCityList(cityList: List<City> ) {
        cityBox.put(cityList)
        cityAdapter.updateData(cityList)
    }

    fun cityDataLoaded(tempCity: CurrentCityData) {
        viewModel.setCurrentCityData(tempCity)
        dialogLoading.hide()
        startActivity(Intent(this, ActivityWeather::class.java))
    }

}