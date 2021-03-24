package com.igorpi25.vincoder.retrofit

import com.igorpi25.vincoder.model.Make
import com.igorpi25.vincoder.model.Manufacturer
import com.igorpi25.vincoder.model.Model
import com.igorpi25.vincoder.model.ServerResponse
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("vehicles/getallmanufacturers")
    fun getAllManufacturers(@Query("format") format: String = "json", @Query("page") page: Int): Call<ServerResponse<Manufacturer>>

    @GET("vehicles/getmodelsformakeid/{makeId}")
    fun getModelsForMakeId(@Path("makeId") makeId: Int, @Query("format") format: String = "json"): Call<ServerResponse<Model>>

    @GET("vehicles/getmakeformanufacturer/{manufacturerId}")
    fun getMakesOfManufacturer(@Path("manufacturerId") manufacturerId: Int, @Query("format") format: String = "json"): Call<ServerResponse<Make>>
}