package com.igorpi25.vincoder.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.igorpi25.vincoder.db.AppDatabase
import com.igorpi25.vincoder.repository.ManufacturerRemoteMediator
import com.igorpi25.vincoder.retrofit.RetrofitService
import kotlinx.coroutines.flow.map
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

    val manufacturerDao = database.manufacturerDao()

    @OptIn(ExperimentalPagingApi::class)
    val flow = Pager(
        config = PagingConfig(
            pageSize = 100
        ),
        remoteMediator = ManufacturerRemoteMediator(database, retrofitService)
    ) {
        manufacturerDao.getPagingSource()
    }.flow.map {
        pagingData -> pagingData.map { UiModel.ManufacturerItem(it) }
    }.map {
        it.insertSeparators<UiModel.ManufacturerItem, UiModel> { before, after ->
            if(after != null && before == null) {
                UiModel.SeparatorItem (1)
            }else if(after == null || before == null) {
                null
            }else if ( before.manufacturer.page != after.manufacturer.page) {
                UiModel.SeparatorItem (after.manufacturer.page?:0)
            } else {
                null
            }
        }
    }.cachedIn(viewModelScope)
}