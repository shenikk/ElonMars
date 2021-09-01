package com.example.elonmars.data.provider

import android.util.Log
import com.example.elonmars.data.PhotoApiInterface
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.data.exception.RequestException
import retrofit2.Retrofit

/**
 * Провайдер данных о загруженных из сети фото.
 *
 * @param retrofitClient клиент для работы с сетью
 */
class GalleryProvider(private val retrofitClient: Retrofit) : IGalleryProvider {

    companion object {
        private const val NUMBER_OF_PHOTOS = 10
    }

    @Throws(java.lang.Exception::class)
    override fun loadPhotoItemsList(): ArrayList<PhotoItem> {
        val weatherRetrofitRequest = retrofitClient.create(PhotoApiInterface::class.java)

        val call = weatherRetrofitRequest.getPhotos(API_KEY, NUMBER_OF_PHOTOS)

        call.execute().let { response ->
            return try {
                if (response.isSuccessful) {
                    response.body() ?: throw RequestException("Body is null")
                } else {
                    Log.e("Response code: ", response.code().toString())
                    throw RequestException("Response code: ${response.code()}")
                }
            } catch (e: java.lang.Exception) {
                Log.e("Return failed", e.toString())
                throw e
            }
        }
    }
}
