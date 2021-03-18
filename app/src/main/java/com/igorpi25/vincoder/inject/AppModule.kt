package com.igorpi25.vincoder.inject

import android.app.Application
import android.content.Context
import android.util.Log
import toothpick.smoothie.module.SmoothieApplicationModule
import com.igorpi25.vincoder.db.AppDatabase

class AppModule(application: Application) : SmoothieApplicationModule(application,"com.igorpi25.vincoder.prefs") {
    init {
        Log.e("Igor log", "AppModule init")
        bind(Context::class.java).toInstance(application)
        bind(AppDatabase::class.java).toProviderInstance(AppDatabaseProvider(application))
    }
}