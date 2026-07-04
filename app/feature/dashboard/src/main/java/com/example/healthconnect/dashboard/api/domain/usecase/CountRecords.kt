package com.example.healthconnect.dashboard.api.domain.usecase

import com.example.healthconnect.dashboard.api.domain.entity.CountResult
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

interface CountRecords {

    operator fun invoke(type: KClass<*>): Flow<CountResult>
}
