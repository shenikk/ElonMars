package com.example.elonmars

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        bottomNavigationView.setupWithNavController(navController)

        setUpNavigation(bottomNavigationView, navController)
    }

    private fun setUpNavigation(bottomNavigationView: BottomNavigationView, navController: NavController) {
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.home_fragment)
                    true
                }
                R.id.gallery -> {
                    navController.navigate(R.id.gallery_fragment)
                    true
                }
                R.id.rover -> {
                    navController.navigate(R.id.rover_fragment)
                    true
                }
                R.id.weather -> {
                    navController.navigate(R.id.weather_fragment)
                    true
                }
                else -> false
            }
        }
    }
}