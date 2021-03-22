package com.igorpi25.vincoder.model

import com.squareup.moshi.Json

data class ServerResponse<T> (
    @field:Json(name = "Count")
    val count: Int,

    @field:Json(name = "Message")
    val message: String,

    @field:Json(name = "Results")
    val results: List<T>
)