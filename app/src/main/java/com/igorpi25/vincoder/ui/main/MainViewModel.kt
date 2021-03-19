package com.igorpi25.vincoder.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.igorpi25.vincoder.repository.ManufacturersPagingSource
import com.igorpi25.vincoder.retrofit.RetrofitService
import com.igorpi25.vincoder.retrofit.model.Manufacturer
import kotlinx.coroutines.flow.map
import toothpick.Toothpick
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var retrofitService: RetrofitService

    init {
        val scope = Toothpick.openScopes("AppScope")
        Toothpick.inject(this, scope);
    }

    val flow = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(pageSize = 1)
    ) {
        ManufacturersPagingSource(retrofitService)
    }.flow.map {
        pagingData -> pagingData.map { UiModel.ManufacturerItem(it) }
    }.map {
        it.insertSeparators<UiModel.ManufacturerItem, UiModel> { before, after ->
            if(after == null || before == null) {
                null
            } else if ( before.manufacturer.id!! < 1000 && after.manufacturer.id!! >= 1000) {
                UiModel.SeparatorItem (2)
            } else {
                null
            }
        }
    }.cachedIn(viewModelScope)
}