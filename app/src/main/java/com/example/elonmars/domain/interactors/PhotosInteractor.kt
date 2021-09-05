package com.example.elonmars.domain.interactors

import android.widget.ImageView
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.domain.repositories.IPhotosRepository
import io.reactivex.Completable
import io.reactivex.Single

class PhotosInteractor(private val photosRepository: IPhotosRepository) : IPhotosInteractor {

    override fun loadPhotosAsync(): Single<ArrayList<PhotoItem>> {
        val favPhotos = photosRepository.getPhotosFromCache()

        return photosRepository.loadPhotosAsync().map {
            it.forEach { photo ->
                if (favPhotos.contains(photo))
                    photo.isFavourite = true
            }

            return@map it
        }
    }

    override fun loadPhotosOnCall(): Single<ArrayList<PhotoItem>> {
        return photosRepository.loadPhotosOnCall()
    }

    override fun loadPhoto(view: ImageView, image: String) {
        return photosRepository.loadPhoto(view, image)
    }

    override fun getFavouritePhotos(): ArrayList<PhotoItem> {
        return ArrayList(photosRepository.getPhotosFromCache().map {
            it.isFavourite = true

            return@map it
        })
    }

    override fun setFavourite(photoItem: PhotoItem) {
        photosRepository.setFavourite(photoItem)
    }
}
