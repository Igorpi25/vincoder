package com.igorpi25.vincoder.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.igorpi25.vincoder.repository.ManufacturersPagingSource
import com.igorpi25.vincoder.retrofit.RetrofitService
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
    }.flow.cachedIn(viewModelScope)
}