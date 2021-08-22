package com.example.elonmars.di

import android.content.Context
import com.example.elonmars.data.repository.ItemsRepository
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

    fun getItemsRepository(): ItemsRepository
}