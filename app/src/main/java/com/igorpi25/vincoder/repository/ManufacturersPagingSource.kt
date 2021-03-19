package com.igorpi25.vincoder.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.igorpi25.vincoder.retrofit.RetrofitService
import com.igorpi25.vincoder.retrofit.model.Manufacturer
import retrofit2.HttpException
import retrofit2.await
import java.io.IOException

class ManufacturersPagingSource(
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
                data = response.results!!,
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
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}