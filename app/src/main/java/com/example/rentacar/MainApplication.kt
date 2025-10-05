package com.example.rentacar

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

/**
 * Main application class for global initialization
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Set default theme mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}

