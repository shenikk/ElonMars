package com.example.elonmars.data.repository

import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.data.provider.IGalleryProvider
import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.domain.repositories.IPhotosRepository
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
}
