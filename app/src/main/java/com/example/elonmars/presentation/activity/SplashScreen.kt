package com.example.elonmars.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/** Экран с логотипом приложения, который показывается при запуске приложения */
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
