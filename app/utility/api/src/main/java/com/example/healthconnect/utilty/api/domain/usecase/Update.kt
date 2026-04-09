package com.example.healthconnect.utilty.api.domain.usecase

import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.api.domain.entity.Result

interface Update {

    suspend operator fun invoke(modifiedRecord: Model): Result
}