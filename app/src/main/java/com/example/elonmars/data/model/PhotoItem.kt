package com.example.elonmars

import com.google.gson.annotations.SerializedName

data class PhotoItem(
    var title: String?,
    var date: String? = null,
    @SerializedName("url")
    var image: String? = null,
    var explanation: String? = null)
