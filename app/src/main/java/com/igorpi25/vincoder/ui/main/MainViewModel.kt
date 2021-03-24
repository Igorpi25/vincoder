package com.igorpi25.vincoder.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.igorpi25.vincoder.db.AppDatabase
import com.igorpi25.vincoder.mediator.ManufacturerRemoteMediator
import com.igorpi25.vincoder.retrofit.RetrofitService
import toothpick.Toothpick
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var retrofitService: RetrofitService

    @Inject
    lateinit var database: AppDatabase

    init {
        val scope = Toothpick.openScopes("AppScope")
        Toothpick.inject(this, scope);
    }

    @OptIn(ExperimentalPagingApi::class)
    val pager = Pager(
        config = PagingConfig(
            pageSize = 100
        ),
        remoteMediator = ManufacturerRemoteMediator(database, retrofitService)
    ) {
        database.manufacturerDao().getPagingSource()
    }

    val flow = MainConverter.convert(pager.flow).cachedIn(viewModelScope)

}