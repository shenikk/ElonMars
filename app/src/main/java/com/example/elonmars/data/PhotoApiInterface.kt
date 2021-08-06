package com.example.elonmars.data

import com.example.elonmars.data.model.PhotoItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("planetary/apod")
    fun getPhotos(
            @Query("api_key") apiKey: String,
            @Query("count") count: Int
    ) : Call<ArrayList<PhotoItem>>
}