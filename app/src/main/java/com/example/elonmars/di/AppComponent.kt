package com.example.elonmars.di

import android.content.Context
import com.example.elonmars.data.repository.ItemsRepository
import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.domain.interactors.ITaskInteractor
import com.example.elonmars.domain.repositories.IItemsRepository
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

    /** Метод для получения [ItemsRepository] */
    fun getItemsRepository(): IItemsRepository

    fun getDataStorage(): IDataStorage

    fun getTaskInteractor(): ITaskInteractor
}