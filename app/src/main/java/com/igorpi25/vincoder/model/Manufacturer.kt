package com.igorpi25.vincoder.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class Manufacturer(
    @PrimaryKey
    @field:Json(name = "Mfr_ID")
    val id: Int,

    @ColumnInfo(name = "name")
    @field:Json(name = "Mfr_Name")
    val name: String,

    @ColumnInfo(name = "country")
    @field:Json(name = "Country")
    val country: String,

    @ColumnInfo(name = "page")
    var page: Int = 0
)