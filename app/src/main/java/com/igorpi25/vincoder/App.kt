package com.igorpi25.vincoder

import android.app.Application
import androidx.room.Room
import com.igorpi25.vincoder.db.AppDatabase

class App : Application() {

    private var database: AppDatabase? = null

    override fun onCreate() {
        super.onCreate()

        instance = this;

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "com.igorpi25.vincoder.db"
        ).build()
    }

    fun getDatabase(): AppDatabase? {
        return database
    }

    companion object {
        private var instance: App? = null

        fun getInstance(): App? {
            return instance
        }
    }

}