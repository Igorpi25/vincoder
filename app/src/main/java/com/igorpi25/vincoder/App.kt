package com.igorpi25.vincoder

import android.app.Application
import com.igorpi25.vincoder.inject.AppModule
import toothpick.Toothpick
import toothpick.configuration.Configuration

class App : Application() {

    private val baseUrl = "https://vpic.nhtsa.dot.gov/api/"

    override fun onCreate() {
        super.onCreate()

        Toothpick.setConfiguration(if(BuildConfig.DEBUG) Configuration.forDevelopment()else Configuration.forProduction())

        val appScope = Toothpick.openScope("AppScope")
        appScope.installModules(AppModule(this, baseUrl))

    }

}