package com.example.elonmars.di

import android.content.Context
import com.example.elonmars.data.database.TasksDbHelper
import com.example.elonmars.data.provider.*
import com.example.elonmars.data.repository.ItemsRepository
import com.example.elonmars.data.repository.TasksRepository
import com.example.elonmars.data.store.DataStorage
import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.domain.interactors.ITaskInteractor
import com.example.elonmars.domain.interactors.TaskInteractor
import com.example.elonmars.domain.repositories.IItemsRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Модуль уровня приложения для реализации DI */
@Module
class AppModule {

    /**
     * Метод для получения репозитория [ItemsRepository],
     * который используется в [ActivityComponent]
     */
    @Provides
    fun provideItemsRepository(context: Context): IItemsRepository {
        return ItemsRepository(getDataStorage(context), getWeatherItemsProvider(), getGalleryProvider())
    }

    @Provides
    fun getDataStorage(context: Context): IDataStorage {
        return DataStorage(context.getSharedPreferences("PREFS", Context.MODE_PRIVATE))
    }

    @Provides
    fun getTaskInteractor(context: Context) : ITaskInteractor {
        return TaskInteractor(getTasksRepository(context))
    }

    private fun getTasksRepository(context: Context): TasksRepository {
        return TasksRepository(getTaskItemsProvider(context))
    }

    private fun getTaskItemsProvider(context: Context): TaskItemsProvider {
        return TaskItemsProvider(getTasksDbHelper(context))
    }

    private fun getTasksDbHelper(context: Context): TasksDbHelper {
        return TasksDbHelper(context)
    }

    private fun getWeatherItemsProvider(): IWeatherItemProvider {
        return WeatherItemsProvider(getWeatherRetrofit())
    }

    private fun getGalleryProvider(): IGalleryProvider {
        return GalleryProvider(getGalleryRetrofit())
    }

    private fun getWeatherRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getGalleryRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PHOTOS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}