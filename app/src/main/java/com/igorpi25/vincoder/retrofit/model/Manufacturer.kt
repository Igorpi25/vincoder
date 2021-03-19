package com.igorpi25.vincoder.retrofit.model

import com.google.gson.annotations.SerializedName

data class Manufacturer(
    @SerializedName("Mfr_ID")
    var id: Int? = null,
    @SerializedName("Mfr_Name")
    var name: String? = null,
    @SerializedName("Country")
    var country: String? = null,
    var page: Int? = null
)