package com.example.elonmars.data.repository

import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.domain.provider.IGalleryProvider
import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.domain.repositories.IPhotosRepository
import io.reactivex.Single

/**
 * Репозиторий - провайдер данных о фото.
 *
 * @param dataStorage хранилище данных
 * @param galleryProvider провайдер фото
 *
 * @testclass unit: PhotosRepositoryTest
 */
class PhotosRepository(
    private val dataStorage: IDataStorage,
    private val galleryProvider: IGalleryProvider
) : IPhotosRepository {

    override fun loadPhotosAsync(): Single<List<PhotoItem>> {
        return Single.fromCallable {
            dataStorage.photos
                ?: galleryProvider.loadPhotoItemsList()
                    .also { dataStorage.photos = it }
        }
    }

    @Throws(java.lang.Exception::class)
    override fun loadPhotosOnCall(): Single<List<PhotoItem>> {
        return Single.fromCallable {
            galleryProvider.loadPhotoItemsList().also { dataStorage.photos = it }
        }
    }

    override fun getPhotosFromCache(): List<PhotoItem> {
        return dataStorage.favouritePhotos ?: arrayListOf()
    }

    override fun setFavourite(photoItem: PhotoItem) {
        if (dataStorage.favouritePhotos?.contains(photoItem) == true) {

            val newList: List<PhotoItem> = dataStorage.favouritePhotos ?: arrayListOf()
            val mutableList = newList.toMutableList()
            mutableList.remove(photoItem)

            dataStorage.favouritePhotos = mutableList
        } else {
            val newList: List<PhotoItem> = dataStorage.favouritePhotos ?: arrayListOf()
            val mutableList = newList.toMutableList()
            mutableList.add(photoItem)

            dataStorage.favouritePhotos = mutableList
        }
    }
}
