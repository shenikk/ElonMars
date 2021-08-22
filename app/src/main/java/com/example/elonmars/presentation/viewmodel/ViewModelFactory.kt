package com.example.elonmars.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elonmars.data.provider.SchedulersProvider
import com.example.elonmars.data.repository.ItemsRepository

class ViewModelFactory(
    private val itemsRepository: ItemsRepository,
    private val schedulersProvider: SchedulersProvider
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GalleryViewModel::class.java) ->
                GalleryViewModel(itemsRepository, schedulersProvider) as T

            modelClass.isAssignableFrom(WeatherViewModel::class.java) ->
                WeatherViewModel(itemsRepository, schedulersProvider) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}