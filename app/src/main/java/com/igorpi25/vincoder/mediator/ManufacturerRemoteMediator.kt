package com.igorpi25.vincoder.mediator

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
            val nextPageNumber : Int = when (loadType) {
                LoadType.REFRESH -> {
                    Log.e("Igor", "ManufacturerRemoteMediator load LoadType.REFRESH loadType=")
                    null
                }
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    state.lastItemOrNull()?.page?.plus(1)
                }
            }?:1

            val response =
                retrofitService.getAllManufacturers(page = nextPageNumber).await()

            response.results.takeIf { it.isNotEmpty() }?.map {it.apply { page = nextPageNumber }}?.let {
                database.withTransaction {
                    manufacturerDao.insertAll(*it.toTypedArray())
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = response.count == 0
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}