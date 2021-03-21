package com.igorpi25.vincoder.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.igorpi25.vincoder.db.dao.ManufacturerDao
import com.igorpi25.vincoder.model.Manufacturer

@Database(entities = arrayOf(Manufacturer::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun manufacturerDao(): ManufacturerDao
}