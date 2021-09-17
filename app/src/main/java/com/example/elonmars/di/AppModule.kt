package com.example.elonmars.di

import android.content.Context
import com.example.elonmars.BuildConfig
import com.example.elonmars.data.database.TasksDbHelper
import com.example.elonmars.data.provider.GalleryProvider
import com.example.elonmars.data.provider.TaskItemsProvider
import com.example.elonmars.data.provider.WeatherItemsProvider
import com.example.elonmars.data.repository.HomeRepository
import com.example.elonmars.data.repository.ItemsRepository
import com.example.elonmars.data.repository.PhotosRepository
import com.example.elonmars.data.repository.TasksRepository
import com.example.elonmars.data.store.DataStorage
import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.domain.interactors.*
import com.example.elonmars.domain.provider.IGalleryProvider
import com.example.elonmars.domain.provider.IWeatherItemsProvider
import com.example.elonmars.domain.repositories.IHomeRepository
import com.example.elonmars.domain.repositories.IPhotosRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Модуль уровня приложения для реализации DI */
@Module
class AppModule {

    /** Метод для получения хранилища данных */
    @Provides
    fun getDataStorage(context: Context): IDataStorage {
        return DataStorage(context.getSharedPreferences("PREFS", Context.MODE_PRIVATE))
    }

    /** Метод для получения интерактора с обработкой фото */
    @Provides
    fun providePhotosInteractor(context: Context): IPhotosInteractor {
        return PhotosInteractor(getPhotosRepository(context))
    }

    /** Метод для получения интерактора с обработкой задач */
    @Provides
    fun getTaskInteractor(context: Context): ITaskInteractor {
        return TaskInteractor(getTasksRepository(context))
    }

    /** Метод для получения интерактора с обработкой погодных данных */
    @Provides
    fun getWeatherInteractor(context: Context): IWeatherInteractor {
        return WeatherInteractor(getItemsRepository(context), getDataStorage(context))
    }

    /** Метод для получения интерактора главного экрана */
    @Provides
    fun getHomeInteractor(context: Context): IHomeInteractor {
        return HomeInteractor(getHomeRepository(context))
    }

    private fun getHomeRepository(context: Context): IHomeRepository {
        return HomeRepository(getDataStorage(context))
    }

    private fun getPhotosRepository(context: Context): IPhotosRepository {
        return PhotosRepository(getDataStorage(context), getGalleryProvider())
    }

    private fun getItemsRepository(context: Context): ItemsRepository {
        return ItemsRepository(getDataStorage(context), getWeatherItemsProvider())
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

    private fun getWeatherItemsProvider(): IWeatherItemsProvider {
        return WeatherItemsProvider(getWeatherRetrofit())
    }

    private fun getGalleryProvider(): IGalleryProvider {
        return GalleryProvider(getGalleryRetrofit())
    }

    private fun getWeatherRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getGalleryRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.PHOTOS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
