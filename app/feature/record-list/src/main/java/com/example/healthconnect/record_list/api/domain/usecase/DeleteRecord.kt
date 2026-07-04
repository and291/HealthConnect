package com.example.healthconnect.record_list.api.domain.usecase

import kotlin.reflect.KClass

interface DeleteRecord {

    suspend operator fun invoke(recordType: KClass<*>, metadataId: String)
}
