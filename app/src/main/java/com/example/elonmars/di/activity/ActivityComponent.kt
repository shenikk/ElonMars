package com.example.elonmars.di.activity

import com.example.elonmars.di.AppComponent
import com.example.elonmars.presentation.viewmodel.ViewModelFactory
import dagger.Component

@Component(
    dependencies = [AppComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {
    /** Метод для получения [ViewModelFactory] */
    fun getViewModelFactory(): ViewModelFactory
}