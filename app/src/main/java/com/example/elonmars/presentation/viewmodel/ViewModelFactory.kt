package com.example.elonmars.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elonmars.data.provider.ISchedulersProvider
import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.domain.interactors.IPhotosInteractor
import com.example.elonmars.domain.interactors.ITaskInteractor
import com.example.elonmars.domain.interactors.IWeatherInteractor
import com.example.elonmars.domain.repositories.IItemsRepository
import com.example.elonmars.domain.repositories.IPhotosRepository

/**
 * Класс фабрика для получения инстансов вьюмоделей
 *
 * @param itemsRepository репозиторий с данными
 * @param schedulersProvider
 */
class ViewModelFactory(
    private val weatherInteractor: IWeatherInteractor,
    private val photosInteractor: IPhotosInteractor,
    private val taskInteractor: ITaskInteractor,
    private val schedulersProvider: ISchedulersProvider,
    private val dataStorage: IDataStorage
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GalleryViewModel::class.java) ->
                GalleryViewModel(photosInteractor, schedulersProvider) as T

            modelClass.isAssignableFrom(WeatherViewModel::class.java) ->
                WeatherViewModel(weatherInteractor, schedulersProvider, dataStorage) as T

            modelClass.isAssignableFrom(MarsMissionViewModel::class.java) ->
                MarsMissionViewModel(taskInteractor, schedulersProvider) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
