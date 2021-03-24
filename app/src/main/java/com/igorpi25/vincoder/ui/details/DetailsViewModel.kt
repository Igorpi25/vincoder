package com.igorpi25.vincoder.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.igorpi25.vincoder.db.AppDatabase
import com.igorpi25.vincoder.mediator.ModelRemoteMediator
import com.igorpi25.vincoder.retrofit.RetrofitService
import toothpick.Toothpick
import javax.inject.Inject

class DetailsViewModel (private val targetManufacturerId: Int) : ViewModel() {

    @Inject
    lateinit var retrofitService: RetrofitService

    @Inject
    lateinit var database: AppDatabase

    init {
        val scope = Toothpick.openScopes("AppScope")
        Toothpick.inject(this, scope);
    }

    val manufacturerDao = database.manufacturerDao()

    val manufacturerName = manufacturerDao.getManufacturer(targetManufacturerId)

    @OptIn(ExperimentalPagingApi::class)
    val pager = Pager(
        config = PagingConfig(
            pageSize = 100
        ),
        remoteMediator = ModelRemoteMediator(targetManufacturerId, database, retrofitService)
    ) {
        database.modelDao().getPagingSource(targetManufacturerId)
    }

    val flow = DetailsConverter.convert(pager.flow,manufacturerName).cachedIn(viewModelScope)
}