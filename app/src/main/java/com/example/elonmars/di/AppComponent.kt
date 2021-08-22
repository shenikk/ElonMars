package com.example.elonmars.di

import android.content.Context
import com.example.elonmars.data.repository.ItemsRepository
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
    fun getItemsRepository(): ItemsRepository
}