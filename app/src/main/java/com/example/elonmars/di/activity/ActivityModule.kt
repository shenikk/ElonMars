package com.example.elonmars.di.activity

import com.example.elonmars.data.provider.ISchedulersProvider
import com.example.elonmars.data.provider.SchedulersProvider
import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.domain.interactors.ITaskInteractor
import com.example.elonmars.domain.interactors.IWeatherInteractor
import com.example.elonmars.domain.repositories.IPhotosRepository
import com.example.elonmars.presentation.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides

/** Модуль уровня активити для реализации DI */
@Module
class ActivityModule {

    @Provides
    fun provideViewModelFactory(
        weatherInteractor: IWeatherInteractor,
        photosRepository: IPhotosRepository,
        dataStorage: IDataStorage,
        taskInteractor: ITaskInteractor
    ): ViewModelFactory {
        return ViewModelFactory(weatherInteractor, photosRepository, getSchedulersProvider(), dataStorage, taskInteractor)
    }

    private fun getSchedulersProvider(): ISchedulersProvider {
        return SchedulersProvider()
    }
}