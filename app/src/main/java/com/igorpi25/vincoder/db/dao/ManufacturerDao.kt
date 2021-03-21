package com.igorpi25.vincoder.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.igorpi25.vincoder.model.Manufacturer

@Dao
interface ManufacturerDao {
    @Query("SELECT * FROM manufacturer")
    fun getAll(): List<Manufacturer>

    @Query("SELECT * FROM manufacturer WHERE page = :pageNumber")
    fun getPage(pageNumber: Int): List<Manufacturer>

    @Query("SELECT * FROM manufacturer")
    fun getPagingSource(): PagingSource<Int, Manufacturer>

    @Query("SELECT * FROM manufacturer WHERE id = :manufacturerId")
    suspend fun loadById(manufacturerId: IntArray): List<Manufacturer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg manufacturers: Manufacturer)

    @Update
    suspend fun updateAll(vararg manufacturers: Manufacturer)

    @Delete
    suspend fun delete(user: Manufacturer)

    @Query("DELETE FROM manufacturer")
    suspend fun clearAll()
}