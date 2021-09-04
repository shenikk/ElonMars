package com.example.elonmars.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Модель, для отображения данных о фото.
 *
 * @param title название фото
 * @param date дата создания
 * @param image адрес фото
 * @param explanation описание фото
 */
@Parcelize
data class PhotoItem(
    var title: String?,
    var date: String? = null,
    @SerializedName("url")
    var image: String? = null,
    var explanation: String? = null,
    var isFavourite: Boolean = false) : Parcelable {

    override fun equals(other: Any?): Boolean{
        other as PhotoItem
        return date == other.date
    }

    override fun hashCode(): Int{
        return date.hashCode()
    }
}
