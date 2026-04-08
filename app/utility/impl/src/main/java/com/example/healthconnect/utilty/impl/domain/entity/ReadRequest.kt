package com.example.healthconnect.utilty.impl.domain.entity

import androidx.health.connect.client.records.Record
import java.time.Instant
import kotlin.reflect.KClass

data class ReadRequest<R : Record>(
    val recordType: KClass<R>,
    val endTime: Instant,
    val dataOriginFilterPackageName: Set<String> = emptySet(),
    val ascendingOrder: Boolean = true,
    val pageSize: Int = 1000,
)