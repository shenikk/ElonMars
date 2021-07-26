package com.example.elonmars

import com.example.elonmars.retrofit.ApiInterface
import com.example.elonmars.retrofit.BASE_URL
import com.example.elonmars.retrofit.RetrofitClient

object Common {
    val retrofit: ApiInterface
        get() = RetrofitClient.getRetrofit(BASE_URL).create(ApiInterface::class.java)
}