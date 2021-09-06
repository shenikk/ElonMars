package com.example.elonmars.domain.interactors

import com.example.elonmars.data.model.PhotoItem
import io.reactivex.Single

interface IPhotosInteractor {
    fun loadPhotosAsync(): Single<ArrayList<PhotoItem>>
    fun loadPhotosOnCall(): Single<ArrayList<PhotoItem>>
    fun getFavouritePhotos(): ArrayList<PhotoItem>
    fun setFavourite(photoItem: PhotoItem)
}
