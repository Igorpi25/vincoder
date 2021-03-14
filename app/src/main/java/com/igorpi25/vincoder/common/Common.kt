package com.igorpi25.vincoder.common

import com.igorpi25.vincoder.interfaces.RetrofitServices
import com.igorpi25.vincoder.retrofit.RetrofitClient

object Common {
    private const val BASE_URL = "https://vpic.nhtsa.dot.gov/api/"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}