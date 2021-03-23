package com.igorpi25.vincoder.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.igorpi25.vincoder.model.Model

@Dao
interface ModelDao {
    @Query("SELECT * FROM model WHERE manufacturer_id = :manufacturerId ORDER BY make_id")
    fun getPagingSource(manufacturerId: Int): PagingSource<Int, Model>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg models: Model)
}