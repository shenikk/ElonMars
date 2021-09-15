package com.example.elonmars.di.activity

import com.example.elonmars.data.provider.SchedulersProvider
import com.example.elonmars.domain.interactors.IHomeInteractor
import com.example.elonmars.domain.interactors.IPhotosInteractor
import com.example.elonmars.domain.interactors.ITaskInteractor
import com.example.elonmars.domain.interactors.IWeatherInteractor
import com.example.elonmars.domain.provider.ISchedulersProvider
import com.example.elonmars.presentation.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides

/** Модуль уровня активити для реализации DI */
@Module
class ActivityModule {

    @Provides
    fun provideViewModelFactory(
        weatherInteractor: IWeatherInteractor,
        photosInteractor: IPhotosInteractor,
        taskInteractor: ITaskInteractor,
        homeInteractor: IHomeInteractor
    ): ViewModelFactory {
        return ViewModelFactory(
            weatherInteractor,
            photosInteractor,
            taskInteractor,
            getSchedulersProvider(),
            homeInteractor
        )
    }

    private fun getSchedulersProvider(): ISchedulersProvider {
        return SchedulersProvider()
    }
}
