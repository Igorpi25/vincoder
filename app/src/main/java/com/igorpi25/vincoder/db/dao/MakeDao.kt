package com.igorpi25.vincoder.db.dao

import androidx.room.*
import com.igorpi25.vincoder.model.Make

@Dao
interface MakeDao {
    @Query("SELECT * FROM make WHERE id = :makeId")
    suspend fun getMakeById(makeId: Int): Make?

    @Query("SELECT * FROM make WHERE manufacturer_id = :manufacturerId")
    suspend fun getFirstMakeOfManufacturer(manufacturerId: Int): Make?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg makes: Make)
}