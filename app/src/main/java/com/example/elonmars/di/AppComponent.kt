package com.example.elonmars.di

import android.content.Context
import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.domain.interactors.IHomeInteractor
import com.example.elonmars.domain.interactors.IPhotosInteractor
import com.example.elonmars.domain.interactors.ITaskInteractor
import com.example.elonmars.domain.interactors.IWeatherInteractor
import dagger.BindsInstance
import dagger.Component

/** Компонент уровня приложения для реализации DI */
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

    fun getPhotosInteractor(): IPhotosInteractor

    fun getTaskInteractor(): ITaskInteractor

    fun getWeatherInteractor(): IWeatherInteractor

    fun getHomeInteractor(): IHomeInteractor
}
