package com.example.healthconnect.utilty.api.domain.usecase

import androidx.health.connect.client.records.Record
import com.example.healthconnect.utilty.api.domain.entity.Result

interface Update {

    suspend operator fun invoke(modifiedRecord: Record): Result
}