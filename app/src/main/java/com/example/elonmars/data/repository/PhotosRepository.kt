package com.example.elonmars.data.repository

import android.widget.ImageView
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.data.provider.IGalleryProvider
import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.domain.repositories.IPhotosRepository
import io.reactivex.Completable
import io.reactivex.Single

class PhotosRepository(
    private val dataStorage: IDataStorage,
    private val galleryProvider: IGalleryProvider
) : IPhotosRepository {

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

    override fun loadPhoto(view: ImageView, image: String) {
        galleryProvider.loadPhoto(view, image)
    }

    override fun getPhotosFromCache(): ArrayList<PhotoItem> {
        return dataStorage.favouritePhotos ?: arrayListOf()
    }

    override fun setFavourite(photoItem: PhotoItem) {
        if (dataStorage.favouritePhotos?.contains(photoItem) == true) {

            val newList = dataStorage.favouritePhotos ?: arrayListOf()
            newList.remove(photoItem)

            dataStorage.favouritePhotos = newList
        } else {
            val newList = dataStorage.favouritePhotos ?: arrayListOf()
            newList.add(photoItem)

            dataStorage.favouritePhotos = newList
        }
    }
}
