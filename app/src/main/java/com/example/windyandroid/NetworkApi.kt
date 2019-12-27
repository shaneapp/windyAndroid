package com.example.windyandroid

import com.example.windyandroid.Data.OpenWeather.ForecastData
import com.example.windyandroid.Data.OpenWeather.WeatherData
import com.example.windyandroid.Data.Unsplash.Photo
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    private const val OPENWEATHER_ENDPOINT = "https://api.openweathermap.org/data/2.5/"

    // TODO: DO NOT COMMIT
    const val UNSPLASH_ACCESS_KEY = "e8376e32bdcc052de5452206ddce81ab439b495e0aaee73e372cc16e4839286c"
    const val OPENWEATHER_API_KEY = "c9c2fc98b49f5cff772627766df7672e"

    val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    val client : OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
    }.build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(UNSPLASH_ENDPOINT)
        .addConverterFactory(createGsonConverter())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val unsplashApi = retrofit.create(UnsplashInterface::class.java)
    val openweatherApi = retrofit.create(OpenWeatherInterface::class.java)

    private fun createGsonConverter(): Converter.Factory {
        return GsonConverterFactory.create(GsonBuilder().create())
    }

    interface UnsplashInterface {
        @GET(UNSPLASH_ENDPOINT + "photos/random")
        fun getImagesList(@Query("client_id") api_key: String, @Query("query") searchTerm: String): Observable<Photo>

        @GET
        fun executeDownloadEvent(@Url url: String, @Query("client_id") api_key: String): Observable<Response<Void>>
    }

    interface OpenWeatherInterface {
        @GET(OPENWEATHER_ENDPOINT + "weather")
        fun getCurrentWeather(@Query("id") city_id: Long, @Query("appid") api_key: String): Observable<WeatherData>

        @GET(OPENWEATHER_ENDPOINT + "forecast")
        fun getHourly4DayForecast(@Query("id") city_id: Long, @Query("appid") api_key: String): Observable<ForecastData>
    }


}