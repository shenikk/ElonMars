package com.example.elonmars.data.provider

import android.util.Log
import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.WeatherApiInterface
import com.example.elonmars.utils.RequestException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Провайдер данных об информации о погоде.
 */
class WeatherItemsProvider : IWeatherItemProvider {

    private val weatherRetrofit = Retrofit.Builder()
        .baseUrl(WEATHER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Throws(java.lang.Exception::class)
    override fun loadWeatherItemsList(): ArrayList<WeatherDataItem> {
        val weatherRetrofitRequest = weatherRetrofit.create(WeatherApiInterface::class.java)
        val call = weatherRetrofitRequest.getWeatherData("weather", "msl", "json")

        call.execute().let { response ->
            return try {
                if (response.isSuccessful) {
                    response.body()?.weatherDataItems ?: throw RequestException("Body is null")
                } else {
                    Log.e("Response code: ", response.code().toString())
                    throw RequestException("Response code: ${response.code()}")
                }
            } catch (e: java.lang.Exception) {
                Log.e("Return failed", e.toString())
                throw e
            }
        }
    }
}