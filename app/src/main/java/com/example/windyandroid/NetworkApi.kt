package com.example.windyandroid

import com.example.windyandroid.Data.Unsplash.Photo
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

object NetworkApi {

    private const val UNSPLASH_ENDPOINT = "https://api.unsplash.com/"

    // TODO: DO NOT COMMIT
    const val UNSPLASH_ACCESS_KEY = "e8376e32bdcc052de5452206ddce81ab439b495e0aaee73e372cc16e4839286c"

    private val retrofit = Retrofit.Builder()
        .client(OkHttpClient().newBuilder().build())
        .baseUrl(UNSPLASH_ENDPOINT)
        .addConverterFactory(createGsonConverter())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val api = retrofit.create(UnsplashInterface::class.java)

    private fun createGsonConverter(): Converter.Factory {
        return GsonConverterFactory.create(GsonBuilder().create())
    }

    interface UnsplashInterface {
        @GET("/photos/random")
        fun getImagesList(@Query("client_id") api_key: String, @Query("query") searchTerm: String): Observable<Photo>

        @GET
        fun executeDownloadEvent(@Url url: String, @Query("client_id") api_key: String): Observable<Response<Void>>
    }


}