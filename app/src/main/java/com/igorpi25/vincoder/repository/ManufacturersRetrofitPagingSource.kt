package com.igorpi25.vincoder.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.igorpi25.vincoder.retrofit.RetrofitService
import com.igorpi25.vincoder.model.Manufacturer
import retrofit2.HttpException
import retrofit2.await
import java.io.IOException

class ManufacturersRetrofitPagingSource(
    val retrofitService: RetrofitService
) : PagingSource<Int, Manufacturer>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Manufacturer> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1

            val response =
                retrofitService.getAllManufacturers(page = nextPageNumber).await()

            return LoadResult.Page(
                data = response.results!!.map {it.apply { page = nextPageNumber }},
                prevKey = null, // Only paging forward.
                nextKey = nextPageNumber + 1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Manufacturer>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}