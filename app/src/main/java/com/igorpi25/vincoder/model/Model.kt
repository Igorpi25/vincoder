package com.igorpi25.vincoder.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class Model(
    @PrimaryKey
    @field:Json(name = "Model_ID")
    val id: Int,

    @ColumnInfo(name = "make_id")
    @field:Json(name = "Make_ID")
    val makeId: Int,

    @ColumnInfo(name = "make_name")
    @field:Json(name = "Make_Name")
    val makeName: String,

    @ColumnInfo(name = "name")
    @field:Json(name = "Model_Name")
    val name: String,

    @ColumnInfo(name = "manufacturer_id")
    var manufacturerId: Int = 0
)