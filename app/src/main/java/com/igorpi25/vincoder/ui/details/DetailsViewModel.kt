package com.igorpi25.vincoder.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.igorpi25.vincoder.db.AppDatabase
import com.igorpi25.vincoder.repository.ModelsRemoteMediator
import com.igorpi25.vincoder.retrofit.RetrofitService
import com.igorpi25.vincoder.ui.common.UiModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
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

    val modelDao = database.modelDao()
    val manufacturerDao = database.manufacturerDao()

    val manufacturerName = manufacturerDao.getManufacturer(targetManufacturerId)

    @OptIn(ExperimentalPagingApi::class)
    val flow = Pager(
        config = PagingConfig(
            pageSize = 100
        ),
        remoteMediator = ModelsRemoteMediator(targetManufacturerId, database, retrofitService)
    ) {
        modelDao.getPagingSource(targetManufacturerId)
    }.flow.map {
        pagingData -> pagingData.map { UiModel.ModelItem(it) }
    }.map {
        it.insertSeparators<UiModel.ModelItem, UiModel> { before, after ->
            if(after != null && before == null) {
                UiModel.MakeItem (after.model.makeName)
            }else if(after == null || before == null) {
                null
            }else if ( before.model.makeId != after.model.makeId) {
                UiModel.MakeItem (after.model.makeName)
            } else {
                null
            }
        }
    }.combine(manufacturerName){
            i, manufacturer -> i.insertHeaderItem(item = UiModel.ManufacturerItem(manufacturer))
    }.cachedIn(viewModelScope)
}