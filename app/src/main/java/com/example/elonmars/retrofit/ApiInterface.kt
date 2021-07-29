package com.example.elonmars.retrofit

import com.example.elonmars.PhotoItem
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