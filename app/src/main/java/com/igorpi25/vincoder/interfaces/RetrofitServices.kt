package com.igorpi25.vincoder.interfaces

import com.igorpi25.vincoder.model.Manufacturer
import com.igorpi25.vincoder.retrofit.ServerResponse
import retrofit2.Call
import retrofit2.http.*

interface RetrofitServices {
    @GET("vehicles/getallmanufacturers")
    fun getAllManufacturers(@Query("format") format: String = "json", @Query("page") page: Int): Call<ServerResponse<Manufacturer>>

    @GET("vehicles/getmanufacturerdetails/{id}")
    fun getManufacturerDetails(@Path("id") id: Int, @Query("format") format: String = "json"): Call<ServerResponse<Manufacturer>>
}