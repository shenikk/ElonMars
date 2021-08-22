package com.example.elonmars.di.activity

import com.example.elonmars.data.provider.SchedulersProvider
import com.example.elonmars.data.repository.ItemsRepository
import com.example.elonmars.presentation.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    @Provides
    fun provideViewModelFactory(
        itemsRepository: ItemsRepository,
        schedulersProvider: SchedulersProvider
    ): ViewModelFactory {
        return ViewModelFactory(itemsRepository, schedulersProvider)
    }
}