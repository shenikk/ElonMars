package com.example.elonmars.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elonmars.data.provider.ISchedulersProvider
import com.example.elonmars.data.repository.IItemsRepository
import com.example.elonmars.data.store.IDataStorage

/**
 * Класс фабрика для получения инстансов вьюмоделей
 *
 * @param itemsRepository репозиторий с данными
 * @param schedulersProvider
 */
class ViewModelFactory(
    private val itemsRepository: IItemsRepository,
    private val schedulersProvider: ISchedulersProvider,
    private val dataStorage: IDataStorage
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GalleryViewModel::class.java) ->
                GalleryViewModel(itemsRepository, schedulersProvider) as T

            modelClass.isAssignableFrom(WeatherViewModel::class.java) ->
                WeatherViewModel(itemsRepository, schedulersProvider, dataStorage) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}