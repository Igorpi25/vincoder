package com.igorpi25.vincoder.inject

import javax.inject.Provider
import com.igorpi25.vincoder.retrofit.RetrofitClient
import com.igorpi25.vincoder.retrofit.RetrofitService

class RetrofitServiceProvider(
    private val baseUrl: String
): Provider<RetrofitService> {

    override fun get(): RetrofitService {
        return RetrofitClient.getClient(baseUrl).create(RetrofitService::class.java)
    }

}