package com.igorpi25.vincoder.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.igorpi25.vincoder.db.AppDatabase
import com.igorpi25.vincoder.db.dao.ManufacturerDao
import com.igorpi25.vincoder.model.Manufacturer
import com.igorpi25.vincoder.retrofit.RetrofitService
import retrofit2.HttpException
import retrofit2.await
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ManufacturerRemoteMediator(
    private val database: AppDatabase,
    private val retrofitService: RetrofitService
) : RemoteMediator<Int, Manufacturer>() {

    private val manufacturerDao: ManufacturerDao = database.manufacturerDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Manufacturer>
    ): MediatorResult {
        return try {
            // The network load method takes an optional String
            // parameter. For every page after the first, pass the String
            // token returned from the previous page to let it continue
            // from where it left off. For REFRESH, pass null to load the
            // first page.
            val nextPageNumber : Int = when (loadType) {
                LoadType.REFRESH -> null
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                // Next page number
                LoadType.APPEND -> {
                    state.lastItemOrNull()?.page?.let { it + 1 } ?: null
                }
            }?:1

            // Suspending network load via Retrofit. This doesn't need to
            // be wrapped in a withContext(Dispatcher.IO) { ... } block
            // since Retrofit's Coroutine CallAdapter dispatches on a
            // worker thread.
            val response =
                retrofitService.getAllManufacturers(page = nextPageNumber).await()

            response.results?.takeIf { it.isNotEmpty() }?.map {it.apply { page = nextPageNumber }}?.let {
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        manufacturerDao.clearAll()
                    }

                    manufacturerDao.insertAll(*it.toTypedArray())
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = response.count?.equals(0)?:false
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}