package com.example.healthconnect.utilty.api.domain.usecase

import com.example.healthconnect.utilty.api.domain.record.Model
import com.example.healthconnect.utilty.api.domain.entity.Result

interface Insert {

    suspend operator fun invoke(record: Model): Result
}