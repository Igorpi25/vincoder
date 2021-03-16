package com.igorpi25.vincoder.db.dao

import androidx.room.*
import com.igorpi25.vincoder.db.entity.Manufacturer

@Dao
interface ManufacturerDao {
    @Query("SELECT * FROM manufacturer")
    suspend fun getAll(): List<Manufacturer>

    @Query("SELECT * FROM manufacturer WHERE id = :manufacturerId")
    suspend fun loadById(manufacturerId: IntArray): List<Manufacturer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg manufacturers: Manufacturer)

    @Update
    suspend fun updateAll(vararg manufacturers: Manufacturer)

    @Delete
    suspend fun delete(user: Manufacturer)
}