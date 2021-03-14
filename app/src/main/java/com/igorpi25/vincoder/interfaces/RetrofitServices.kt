package com.igorpi25.vincoder.interfaces

import com.igorpi25.vincoder.model.Movie
import retrofit2.Call
import retrofit2.http.*

interface RetrofitServices {
    @GET("marvel")
    fun getMovieList(): Call<MutableList<Movie>>
}