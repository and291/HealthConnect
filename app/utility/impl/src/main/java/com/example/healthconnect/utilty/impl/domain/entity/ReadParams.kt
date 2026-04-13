package com.example.healthconnect.utilty.impl.domain.entity

import com.example.healthconnect.models.api.domain.record.Model
import java.time.Instant
import kotlin.reflect.KClass

data class ReadParams<M : Model>(
    val modelType: KClass<M>,
    val endTime: Instant,
    val dataOriginFilterPackageName: Set<String> = emptySet(),
    val ascendingOrder: Boolean = true,
    val pageSize: Int = 1000,
)