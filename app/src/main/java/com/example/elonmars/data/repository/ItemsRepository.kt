package com.example.elonmars.data.repository

import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.WeatherItem
import com.example.elonmars.data.provider.GalleryProvider
import com.example.elonmars.data.provider.WeatherItemsProvider
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Репозиторий - провайдер данных о скачанной информации по фото и погоде.
 */
class ItemsRepository : IItemsRepository {
    private val weatherItemsProvider = WeatherItemsProvider()
    private val galleryProvider = GalleryProvider()

    override fun loadDataAsync(): Single<ArrayList<WeatherItem>> {
        return Single.fromCallable { weatherItemsProvider.loadWeatherItemsList() }
    }

    override fun loadPhotosAsync(): Single<ArrayList<PhotoItem>> {
        return Single.fromCallable { galleryProvider.loadPhotoItemsList() }
    }
}