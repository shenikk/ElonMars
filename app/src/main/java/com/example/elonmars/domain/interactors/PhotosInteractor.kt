package com.example.elonmars.domain.interactors

import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.domain.repositories.IPhotosRepository
import io.reactivex.Single

/**
 * Интерактор для экрана галереи с фото.
 *
 * @param photosRepository хранилище данных
 *
 * @testClass unit: PhotosInteractorTest
 */
class PhotosInteractor(private val photosRepository: IPhotosRepository) : IPhotosInteractor {

    override fun loadPhotosAsync(): Single<List<PhotoItem>> {
        val favPhotos = photosRepository.getFavouritePhotosFromCache()

        return photosRepository.loadPhotosAsync().map {
            it.forEach { photo ->
                if (favPhotos.contains(photo))
                    photo.isFavourite = true
            }

            return@map it
        }.run {
            filterMediaType(this)
        }
    }

    override fun loadPhotosOnCall(): Single<List<PhotoItem>> {
        return photosRepository.loadPhotosOnCall().run {
            filterMediaType(this)
        }
    }

    override fun getFavouritePhotos(): List<PhotoItem> {
        return photosRepository.getFavouritePhotosFromCache().map {
            it.isFavourite = true

            return@map it
        }
    }

    override fun setFavourite(photoItem: PhotoItem) {
        photosRepository.setFavourite(photoItem)
    }

    override fun setGalleryType(type: Int) {
        photosRepository.setGalleryType(type)
    }

    override fun getGalleryType(): Int {
        return photosRepository.getGalleryType()
    }

    /** Возвращает данные с медиатипом "image" */
    private fun filterMediaType(data: Single<List<PhotoItem>>): Single<List<PhotoItem>> {
        return data.map {
            return@map it.filter { photo ->
                photo.mediaType == "image"
            }
        }
    }
}
