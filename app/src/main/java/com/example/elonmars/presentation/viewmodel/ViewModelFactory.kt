package com.example.elonmars.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elonmars.domain.interactors.IHomeInteractor
import com.example.elonmars.domain.interactors.IPhotosInteractor
import com.example.elonmars.domain.interactors.ITaskInteractor
import com.example.elonmars.domain.interactors.IWeatherInteractor
import com.example.elonmars.domain.provider.ISchedulersProvider

/**
 * Класс фабрика для получения инстансов вьюмоделей.
 *
 * @param weatherInteractor интерактор, обрабатыващий погодные данные.
 * @param photosInteractor интерактор, обрабатывающий данные с фото.
 * @param taskInteractor интерактор, обрабатывающий данные с задачами.
 * @param schedulersProvider провайдер с Scheduler для работы на разных потоках.
 * @param homeInteractor интерактор главного экрана.
 */
class ViewModelFactory(
    private val weatherInteractor: IWeatherInteractor,
    private val photosInteractor: IPhotosInteractor,
    private val taskInteractor: ITaskInteractor,
    private val schedulersProvider: ISchedulersProvider,
    private val homeInteractor: IHomeInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GalleryViewModel::class.java) ->
                GalleryViewModel(photosInteractor, schedulersProvider) as T

            modelClass.isAssignableFrom(DetailPhotoViewModel::class.java) ->
                DetailPhotoViewModel(photosInteractor, schedulersProvider) as T

            modelClass.isAssignableFrom(WeatherViewModel::class.java) ->
                WeatherViewModel(weatherInteractor, schedulersProvider) as T

            modelClass.isAssignableFrom(MarsMissionViewModel::class.java) ->
                MarsMissionViewModel(taskInteractor, schedulersProvider) as T

            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(homeInteractor) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
