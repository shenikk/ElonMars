package com.example.elonmars.retrofit

import com.example.elonmars.PhotoItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.nasa.gov/"
const val API_KEY = "S0pO0YrtiG1iqfuIokCyaD1xM0D4xob8ywxlM6uf"

interface ApiInterface {

    @GET("planetary/apod")
    fun getPhotos(
            @Query("api_key") apiKey: String,
            @Query("count") count: Int
    ) : Call<ArrayList<PhotoItem>>
}