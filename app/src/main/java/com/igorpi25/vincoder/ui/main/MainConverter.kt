package com.igorpi25.vincoder.ui.main

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.igorpi25.vincoder.model.Manufacturer
import com.igorpi25.vincoder.ui.common.UiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object MainConverter {
    fun convert(source: Flow<PagingData<Manufacturer>>): Flow<PagingData<UiModel>> {
        return source.map {
                pagingData -> pagingData.map { UiModel.ManufacturerItem(it) }
        }.map {
            it.insertSeparators<UiModel.ManufacturerItem, UiModel> { before, after ->
                if(after != null && before == null) {
                    UiModel.PageItem (1)
                }else if(after == null || before == null) {
                    null
                }else if ( before.manufacturer.page != after.manufacturer.page) {
                    UiModel.PageItem (after.manufacturer.page)
                } else {
                    null
                }
            }
        }
    }
}