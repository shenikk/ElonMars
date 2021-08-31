package com.example.elonmars.domain.interactors

import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.domain.repositories.IPhotosRepository
import io.reactivex.Single

class PhotosInteractor(private val photosRepository: IPhotosRepository) : IPhotosInteractor {

    override fun loadPhotosAsync(): Single<ArrayList<PhotoItem>> {
        return photosRepository.loadPhotosAsync()
    }

    override fun loadPhotosOnCall(): Single<ArrayList<PhotoItem>> {
        return photosRepository.loadPhotosOnCall()
    }
}