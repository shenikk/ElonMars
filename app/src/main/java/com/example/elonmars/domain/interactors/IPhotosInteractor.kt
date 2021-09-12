package com.example.elonmars.domain.interactors

import com.example.elonmars.data.model.PhotoItem
import io.reactivex.Observable
import io.reactivex.Single

interface IPhotosInteractor {
    fun loadPhotosAsync(): Observable<ArrayList<PhotoItem>>
    fun loadPhotosOnCall(): Single<ArrayList<PhotoItem>>
    fun getFavouritePhotos(): ArrayList<PhotoItem>
    fun setFavourite(photoItem: PhotoItem)
}
