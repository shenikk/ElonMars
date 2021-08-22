package com.example.elonmars

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.elonmars.di.AppComponent
import com.example.elonmars.di.DaggerAppComponent

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
}