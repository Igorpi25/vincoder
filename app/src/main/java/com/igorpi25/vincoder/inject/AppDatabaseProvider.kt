package com.igorpi25.vincoder.inject

import android.content.Context
import androidx.room.Room
import javax.inject.Provider
import com.igorpi25.vincoder.db.AppDatabase

class AppDatabaseProvider(private val context: Context): Provider<AppDatabase> {

    companion object {
        private  var database: AppDatabase? = null
    }

    override fun get(): AppDatabase {
        if( database == null ) {
            database =  Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "com.igorpi25.vincoder.db"
            ).build()
        }

        return database!!
    }

}