package com.igorpi25.vincoder.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.igorpi25.vincoder.db.AppDatabase
import com.igorpi25.vincoder.model.Manufacturer

class ManufacturersRoomPagingSource(
    private val database: AppDatabase
) : PagingSource<Int, Manufacturer>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Manufacturer> {
        // Start refresh at page 1 if undefined.
        val nextPageNumber = params.key ?: 1

        val manufacturers = database.manufacturerDao().getPage(nextPageNumber)

        return LoadResult.Page(
            data = manufacturers,
            prevKey = null, // Only paging forward.
            nextKey = nextPageNumber + 1
        )
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