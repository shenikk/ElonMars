package com.example.elonmars.data.repository

import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.data.provider.IGalleryProvider
import com.example.elonmars.data.provider.IWeatherItemProvider
import com.example.elonmars.data.store.IDataStorage
import io.reactivex.Single

/**
 * Репозиторий - провайдер данных о скачанной информации по фото и погоде.
 *
 * @param dataStorage хранилище данных
 * @param weatherItemsProvider провайдер погодных данных
 * @param galleryProvider провайдер данных с фото
 */
class ItemsRepository(
    private val dataStorage: IDataStorage,
    private val weatherItemsProvider: IWeatherItemProvider,
    private val galleryProvider: IGalleryProvider
) : IItemsRepository {

    override fun loadDataAsync(): Single<ArrayList<WeatherDataItem>> {
        return Single.fromCallable {
            dataStorage.weatherDataItems
                ?: ArrayList(weatherItemsProvider.loadWeatherItemsList().take(10))
                    .also { dataStorage.weatherDataItems = it }
        }
    }

    override fun loadDataAsyncOnCall(): Single<ArrayList<WeatherDataItem>> {
        return Single.fromCallable {
            ArrayList(weatherItemsProvider.loadWeatherItemsList().take(10))
                .also { dataStorage.weatherDataItems = it }
        }
    }


    override fun loadPhotosAsync(): Single<ArrayList<PhotoItem>> {
        return Single.fromCallable {
            dataStorage.photos
                ?: galleryProvider.loadPhotoItemsList()
                    .also { dataStorage.photos = it }
        }
    }

    @Throws(java.lang.Exception::class)
    override fun loadPhotosOnCall(): Single<ArrayList<PhotoItem>> {
        return Single.fromCallable {
            galleryProvider.loadPhotoItemsList().also { dataStorage.photos = it }
        }
    }
}