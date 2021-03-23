package com.igorpi25.vincoder.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.igorpi25.vincoder.db.dao.MakeDao
import com.igorpi25.vincoder.db.dao.ManufacturerDao
import com.igorpi25.vincoder.db.dao.ModelDao
import com.igorpi25.vincoder.model.Make
import com.igorpi25.vincoder.model.Manufacturer
import com.igorpi25.vincoder.model.Model

@Database(entities = arrayOf(Manufacturer::class, Make::class, Model::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun manufacturerDao(): ManufacturerDao
    abstract fun makeDao(): MakeDao
    abstract fun modelDao(): ModelDao
}