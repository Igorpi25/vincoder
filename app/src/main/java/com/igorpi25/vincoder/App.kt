package com.igorpi25.vincoder

import android.app.Application
import androidx.room.Room
import com.igorpi25.vincoder.db.AppDatabase
import com.igorpi25.vincoder.inject.AppModule
import toothpick.Toothpick
import toothpick.configuration.Configuration

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Toothpick.setConfiguration(if(BuildConfig.DEBUG) Configuration.forDevelopment()else Configuration.forProduction())

        val appScope = Toothpick.openScope("AppScope")
        appScope.installModules(AppModule(this))
        Toothpick.inject(this, appScope)

    }

}