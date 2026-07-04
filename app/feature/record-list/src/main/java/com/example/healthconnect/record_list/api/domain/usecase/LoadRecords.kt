package com.example.healthconnect.record_list.api.domain.usecase

import com.example.healthconnect.record_list.api.domain.entity.RecordPager
import kotlin.reflect.KClass

interface LoadRecords {

    operator fun invoke(recordType: KClass<*>): RecordPager
}
