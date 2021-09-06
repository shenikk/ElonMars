package com.example.elonmars.data.provider

import com.example.elonmars.WeatherData
import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.WeatherApiInterface
import com.example.elonmars.data.exception.RequestException
import com.example.elonmars.presentation.extensions.logError
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Провайдер данных об информации о погоде.
 *
 * @param retrofitClient клиент для работы с сетью
 *
 * @testclass unit: WeatherItemsProviderTest
 */
class WeatherItemsProvider(private val retrofitClient: Retrofit) : IWeatherItemProvider {

    @Throws(java.lang.Exception::class)
    override fun loadWeatherItemsList(): ArrayList<WeatherDataItem> {
        val weatherRetrofitRequest = retrofitClient.create(WeatherApiInterface::class.java)
        val call = weatherRetrofitRequest.getWeatherData("weather", "msl", "json")

        call.execute().let { response ->
            return executeCall(response)
        }
    }

    private fun executeCall(response: Response<WeatherData>): ArrayList<WeatherDataItem> {
        return try {
            if (response.isSuccessful) {
                if (response.body()?.weatherDataItems.isNullOrEmpty()) {
                    throw RequestException("body is empty")
                }
                response.body()?.weatherDataItems ?: throw RequestException("Body is null")
            } else {
                logError("Response code: ${response.code()}")
                throw RequestException("Response code: ${response.code()}")
            }
        } catch (e: java.lang.Exception) {
            logError("Return failed", e)
            throw e
        }
    }
}
