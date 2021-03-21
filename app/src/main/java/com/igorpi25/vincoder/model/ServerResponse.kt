package com.igorpi25.vincoder.model

import com.google.gson.annotations.SerializedName

data class ServerResponse<T> (
    @SerializedName("Count")
    var count: Int? = null,
    @SerializedName("Message")
    var message: String? = null,
    @SerializedName("Results")
    var results: List<T>? = null
)