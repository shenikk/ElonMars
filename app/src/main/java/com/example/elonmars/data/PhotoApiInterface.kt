package com.example.elonmars.data

import com.example.elonmars.data.model.PhotoItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/** Интерфейс запроса в сеть для получения списка фото */
interface PhotoApiInterface {

    /**
     * Метод описывающий запрос в сеть для получения списка фото.
     *
     * @param apiKey индивидуальный ключ для работы с api nasa
     * @param count количество рандомно выбранных фото
     * @return Call со списком фото
     */
    @GET("planetary/apod")
    fun getPhotos(
            @Query("api_key") apiKey: String,
            @Query("count") count: Int
    ) : Call<ArrayList<PhotoItem>>
}