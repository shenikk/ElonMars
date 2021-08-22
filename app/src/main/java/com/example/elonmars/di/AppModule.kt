package com.example.elonmars.di

import android.content.Context
import com.example.elonmars.data.provider.GalleryProvider
import com.example.elonmars.data.provider.PHOTOS_BASE_URL
import com.example.elonmars.data.provider.WEATHER_BASE_URL
import com.example.elonmars.data.provider.WeatherItemsProvider
import com.example.elonmars.data.repository.ItemsRepository
import com.example.elonmars.data.store.DataStorageImpl
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
    fun provideItemsRepository(context: Context): ItemsRepository {
        return ItemsRepository(getDataStorage(context), getWeatherItemsProvider(), getGalleryProvider())
    }

    private fun getDataStorage(context: Context): DataStorageImpl {
        return DataStorageImpl(context.getSharedPreferences("PREFS", Context.MODE_PRIVATE))
    }

    private fun getWeatherItemsProvider(): WeatherItemsProvider {
        return WeatherItemsProvider(getWeatherRetrofit())
    }

    private fun getGalleryProvider(): GalleryProvider {
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