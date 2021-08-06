package com.example.elonmars.data.model

import com.google.gson.annotations.SerializedName

/**
 * Модель, для отображения данных о фото.
 *
 * @param title название фото
 * @param date дата создания
 * @param image адрес фото
 * @param explanation описание фото
 */
data class PhotoItem(
    var title: String?,
    var date: String? = null,
    @SerializedName("url")
    var image: String? = null,
    var explanation: String? = null)
