package com.example.elonmars.data

import com.example.elonmars.data.model.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/** Интерфейс запроса в сеть для получения информации о погоде */
interface WeatherApiInterface {

    /**
     * Метод описывающий запрос в сеть для получения данных о погоде.
     *
     * @param feed
     * @param category
     * @param feedtype формат возвращаемого значения (json)
     * @return Call с моделью данных [WeatherData]
     */
    @GET("rss/api")
    fun getWeatherData(
            @Query("feed") feed: String,
            @Query("category") category: String,
            @Query("feedtype") feedtype: String
    ) : Call<WeatherData>
}
