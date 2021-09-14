package com.example.elonmars.domain.interactors

import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.domain.repositories.IPhotosRepository
import io.reactivex.Observable
import io.reactivex.Single

class PhotosInteractor(private val photosRepository: IPhotosRepository) : IPhotosInteractor {

    override fun loadPhotosAsync(): Observable<List<PhotoItem>> {
        val favPhotos = photosRepository.getPhotosFromCache()

        return photosRepository.loadPhotosAsync().map {
            it.forEach { photo ->
                if (favPhotos.contains(photo))
                    photo.isFavourite = true
            }

            return@map it
        }
    }

    override fun loadPhotosOnCall(): Single<List<PhotoItem>> {
        return photosRepository.loadPhotosOnCall()
    }

    override fun getFavouritePhotos(): List<PhotoItem> {
        return photosRepository.getPhotosFromCache().map {
            it.isFavourite = true

            return@map it
        }
    }

    override fun setFavourite(photoItem: PhotoItem) {
        photosRepository.setFavourite(photoItem)
    }
}
