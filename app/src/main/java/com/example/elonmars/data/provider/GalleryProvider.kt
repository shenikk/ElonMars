package com.example.elonmars.data.provider

import com.example.elonmars.BuildConfig
import com.example.elonmars.data.PhotoApiInterface
import com.example.elonmars.data.exception.RequestException
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.presentation.extensions.logError
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

        val call = weatherRetrofitRequest.getPhotos(BuildConfig.API_KEY, NUMBER_OF_PHOTOS)

        call.execute().let { response ->
            return try {
                if (response.isSuccessful) {
                    response.body() ?: throw RequestException("Body is null")
                } else {
                    logError("Response code: ${response.code()} ")
                    throw RequestException("Response code: ${response.code()}")
                }
            } catch (e: java.lang.Exception) {
                logError("Return failed", e)
                throw e
            }
        }
    }
}
