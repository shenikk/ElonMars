package com.example.elonmars

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.elonmars.di.app.AppComponent
import com.example.elonmars.di.app.DaggerAppComponent

/**
 * Класс [Application] для инициализации компонентов,
 * которые необходимы на всем жизненном цикле приложения.
 */
class MyApplication : Application() {

    private lateinit var appComponent: AppComponent

    companion object {
        fun getAppComponent(context: Context): AppComponent {
            return (context.applicationContext as MyApplication).appComponent
        }
    }

    override fun onCreate() {
        super.onCreate()

        setTheme()
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }

    private fun setTheme() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val nightMode = sharedPreferences.getBoolean("DarkTheme", false)
        if (nightMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
    // test commit2
    // test commit3
    //sjsjs
}
