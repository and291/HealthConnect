package com.example.healthconnect.utilty.impl.domain.entity

import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import kotlinx.coroutines.flow.Flow

interface Pager {
    val pages: Flow<FlowResult<Page>>
    fun requestNextPage()
}