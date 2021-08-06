package com.example.elonmars.data.provider

import android.util.Log
import com.example.elonmars.data.PhotoApiInterface
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.utils.RequestException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Провайдер данных о загруженных из сети фото.
 */
class GalleryProvider : IGalleryProvider {

   // TODO добавить кэширование/ сохранение фото в бд
    private var NUMBER_OF_PHOTOS = 10
    private val galleryRetrofit = Retrofit.Builder()
        .baseUrl(PHOTOS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Throws(java.lang.Exception::class)
    override fun loadPhotoItemsList(): ArrayList<PhotoItem> {
        val weatherRetrofitRequest = galleryRetrofit.create(PhotoApiInterface::class.java)

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