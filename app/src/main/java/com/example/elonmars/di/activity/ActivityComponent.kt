package com.example.elonmars.di.activity

import com.example.elonmars.di.app.AppComponent
import com.example.elonmars.presentation.factory.ViewModelFactory
import dagger.Component

/** Компонент уровня активити для реализации DI */
@Component(
    dependencies = [AppComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {
    /** Метод для получения [ViewModelFactory] */
    fun getViewModelFactory(): ViewModelFactory
}
