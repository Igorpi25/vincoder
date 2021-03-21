package com.igorpi25.vincoder.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Manufacturer(
    @PrimaryKey
    @SerializedName("Mfr_ID")
    var id: Int? = null,

    @ColumnInfo(name = "name")
    @SerializedName("Mfr_Name")
    var name: String? = null,

    @ColumnInfo(name = "country")
    @SerializedName("Country")
    var country: String? = null,

    @ColumnInfo(name = "page")
    var page: Int? = null
)