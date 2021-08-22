package com.example.elonmars.data.provider

import android.util.Log
import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.WeatherApiInterface
import com.example.elonmars.data.exception.RequestException
import retrofit2.Retrofit

/**
 * Провайдер данных об информации о погоде.
 *
 * @param retrofitClient клиент для работы с сетью
 */
class WeatherItemsProvider(private val retrofitClient: Retrofit) : IWeatherItemProvider {

    @Throws(java.lang.Exception::class)
    override fun loadWeatherItemsList(): ArrayList<WeatherDataItem> {
        val weatherRetrofitRequest = retrofitClient.create(WeatherApiInterface::class.java)
        val call = weatherRetrofitRequest.getWeatherData("weather", "msl", "json")

        call.execute().let { response ->
            return try {
                if (response.isSuccessful) {
                    if (response.body()?.weatherDataItems.isNullOrEmpty()) {
                        throw RequestException("body is empty")
                    }
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