package com.example.windyandroid.View

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.windyandroid.R
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    protected val compositeDisposable = CompositeDisposable()
    private lateinit var dialogLoading: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupLoadingDialog()
    }

    override fun onPause() {
        compositeDisposable.clear()
        super.onPause()
    }

    private fun setupLoadingDialog() {
        dialogLoading = Dialog(this)
        dialogLoading.setContentView(R.layout.dialog_progress)
        dialogLoading.setCancelable(false)
    }

    fun showLoadingDialog() = dialogLoading.show()
    fun hideLoadingDialog() = dialogLoading.hide()

}