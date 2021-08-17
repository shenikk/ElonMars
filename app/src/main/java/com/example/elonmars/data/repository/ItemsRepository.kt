package com.example.elonmars.data.repository

import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.provider.GalleryProvider
import com.example.elonmars.data.provider.WeatherItemsProvider
import io.reactivex.Single

/**
 * Репозиторий - провайдер данных о скачанной информации по фото и погоде.
 */
class ItemsRepository : IItemsRepository {
    private val weatherItemsProvider = WeatherItemsProvider()
    private val galleryProvider = GalleryProvider()

    override fun loadDataAsync(): Single<ArrayList<WeatherDataItem>> {
        return Single.fromCallable { ArrayList(weatherItemsProvider.loadWeatherItemsList().take(10)) }
    }

    override fun loadPhotosAsync(): Single<ArrayList<PhotoItem>> {
        return Single.fromCallable { galleryProvider.loadPhotoItemsList() }
    @Throws(java.lang.Exception::class)
    override fun loadPhotosOnCall(): Single<ArrayList<PhotoItem>> {
        return Single.fromCallable { galleryProvider.loadPhotoItemsList().also(dataStorage::savePhotos) }
    }
}