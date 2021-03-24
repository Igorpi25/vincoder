package com.igorpi25.vincoder.ui.details

import androidx.paging.PagingData
import androidx.paging.insertHeaderItem
import androidx.paging.insertSeparators
import androidx.paging.map
import com.igorpi25.vincoder.model.Manufacturer
import com.igorpi25.vincoder.model.Model
import com.igorpi25.vincoder.ui.common.UiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

object DetailsConverter {
    fun convert(source: Flow<PagingData<Model>>, manufacturerName: Flow<Manufacturer>): Flow<PagingData<UiModel>> {
        return source.map {
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
        }
    }
}