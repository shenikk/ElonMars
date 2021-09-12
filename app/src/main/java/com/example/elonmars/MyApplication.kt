package com.example.elonmars

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.preference.PreferenceManager
import com.example.elonmars.di.AppComponent
import com.example.elonmars.di.DaggerAppComponent
import com.example.elonmars.presentation.viewmodel.GalleryViewModel
import com.example.elonmars.presentation.viewmodel.ViewModelFactory

/**
 * Класс [Application] для инициализации компонентов,
 * которые необходимы на всем жизненном цикле приложения.
 */
class MyApplication : Application() {

    private lateinit var appComponent: AppComponent
    private lateinit var viewModelFactory: ViewModelFactory
//    private lateinit var activityComponent: ActivityComponent

    companion object {
        fun getAppComponent(context: Context): AppComponent {
            return (context.applicationContext as MyApplication).appComponent
        }

//        fun getActivityComponent(context: Context): ActivityComponent {
//            return (context.applicationContext as MyApplication).activityComponent
//        }

        fun getViewModelFactory(context: Context, viewModelStoreOwner: ViewModelStoreOwner): GalleryViewModel {
            val viewModelFactory = (context.applicationContext as MyApplication).appComponent.getViewModelFactory()
            return ViewModelProvider(viewModelStoreOwner, viewModelFactory).get(GalleryViewModel::class.java)
        }
    }

    override fun onCreate() {
        super.onCreate()

        setTheme()
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()

//        activityComponent = DaggerActivityComponent.builder()
//            .appComponent(appComponent)
//            .build()

        viewModelFactory = appComponent.getViewModelFactory()
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
