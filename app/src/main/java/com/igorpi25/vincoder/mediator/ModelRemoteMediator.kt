package com.igorpi25.vincoder.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.igorpi25.vincoder.db.AppDatabase
import com.igorpi25.vincoder.db.dao.MakeDao
import com.igorpi25.vincoder.db.dao.ModelDao
import com.igorpi25.vincoder.model.Model
import com.igorpi25.vincoder.retrofit.RetrofitService
import retrofit2.HttpException
import retrofit2.await
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ModelRemoteMediator(
    private val targetManufacturer: Int,
    private val database: AppDatabase,
    private val retrofitService: RetrofitService
) : RemoteMediator<Int, Model>() {

    private val modelDao: ModelDao = database.modelDao()
    private val makeDao: MakeDao = database.makeDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Model>
    ): MediatorResult {
        return try {
            val nextMakeId : Int = when (loadType) {
                LoadType.REFRESH -> {
                    val firstMake = makeDao.getFirstMakeOfManufacturer(targetManufacturer)

                    if (firstMake == null ) {
                        val response = retrofitService.getMakesOfManufacturer(manufacturerId = targetManufacturer).await()

                        if (response.results.isNotEmpty()) {
                            val makes = response.results
                            makes.zipWithNext{a, b ->  a.apply { nextId = b.id }}
                            database.withTransaction {
                                makeDao.insertAll(*makes.plusElement(response.results.last()).map { it.apply { it.manufacturerId = targetManufacturer } }.toTypedArray())
                            }

                            makes.first().id
                        } else {
                            return MediatorResult.Success(
                                endOfPaginationReached = true
                            )
                        }

                    } else {
                        firstMake.id
                    }
                }
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastMakeId  = state.lastItemOrNull()?.makeId

                    if (lastMakeId == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    val nextMakeId = makeDao.getMakeById(lastMakeId)?.nextId
                    if(nextMakeId == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    nextMakeId
                }
            }

            val response =
                retrofitService.getModelsForMakeId(makeId = nextMakeId).await()

            response.results.takeIf { it.isNotEmpty() }?.let {
                database.withTransaction {
                    modelDao.insertAll(*it.map { it.apply { it.manufacturerId = targetManufacturer } }.toTypedArray())
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = response.results.size == 0
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}