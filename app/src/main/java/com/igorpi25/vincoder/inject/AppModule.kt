package com.igorpi25.vincoder.inject

import android.app.Application
import android.content.Context
import toothpick.smoothie.module.SmoothieApplicationModule
import com.igorpi25.vincoder.db.AppDatabase
import com.igorpi25.vincoder.retrofit.RetrofitService

class AppModule(application: Application, baseUrl: String) : SmoothieApplicationModule(application,"com.igorpi25.vincoder.prefs") {
    init {
        bind(Context::class.java).toInstance(application)
        bind(AppDatabase::class.java).toProviderInstance(AppDatabaseProvider(application))
        bind(RetrofitService::class.java).toProviderInstance(RetrofitServiceProvider(baseUrl))
    }
}