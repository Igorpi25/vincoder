package com.igorpi25.vincoder.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.igorpi25.vincoder.model.Manufacturer

data class ServerResponse<T> (
    @SerializedName("Count")
    var count: Int? = null,
    @SerializedName("Message")
    var message: String? = null,
    @SerializedName("Results")
    var results: List<T>? = null
)