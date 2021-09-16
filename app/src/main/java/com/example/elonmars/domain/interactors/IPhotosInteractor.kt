package com.example.elonmars.domain.interactors

import com.example.elonmars.data.model.PhotoItem
import io.reactivex.Observable
import io.reactivex.Single

interface IPhotosInteractor {
    fun loadPhotosAsync(): Single<List<PhotoItem>>
    fun loadPhotosOnCall(): Single<List<PhotoItem>>
    fun getFavouritePhotos(): List<PhotoItem>
    fun setFavourite(photoItem: PhotoItem)
    fun setGalleryType(type: Int)
    fun getGalleryType(): Int
}
