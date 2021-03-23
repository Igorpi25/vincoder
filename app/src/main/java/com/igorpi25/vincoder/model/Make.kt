package com.igorpi25.vincoder.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class Make(
    @PrimaryKey
    @field:Json(name = "Make_ID")
    val id: Int,

    @ColumnInfo(name = "manufacturer_name")
    @field:Json(name = "Mfr_Name")
    val manufacturerName: String,

    @ColumnInfo(name = "name")
    @field:Json(name = "Make_Name")
    val name: String,

    @ColumnInfo(name = "next_id")
    var nextId: Int? = null,

    @ColumnInfo(name = "manufacturer_id")
    var manufacturerId: Int = 0
)