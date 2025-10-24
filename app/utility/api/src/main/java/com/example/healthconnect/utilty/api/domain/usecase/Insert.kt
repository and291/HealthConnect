package com.example.healthconnect.utilty.api.domain.usecase

import androidx.health.connect.client.records.Record
import com.example.healthconnect.utilty.api.domain.entity.Result

interface Insert {

    suspend operator fun invoke(record: Record): Result
}