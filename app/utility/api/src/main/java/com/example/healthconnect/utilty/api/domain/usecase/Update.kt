package com.example.healthconnect.utilty.api.domain.usecase

import com.example.healthconnect.utilty.api.record.Model
import com.example.healthconnect.utilty.api.domain.entity.Result

interface Update {

    suspend operator fun invoke(modifiedRecord: Model): Result
}