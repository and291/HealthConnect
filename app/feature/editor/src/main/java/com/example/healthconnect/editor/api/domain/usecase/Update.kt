package com.example.healthconnect.editor.api.domain.usecase

import com.example.healthconnect.editor.api.domain.entity.Editable
import com.example.healthconnect.editor.api.domain.entity.Result

interface Update {

    suspend operator fun invoke(modifiedRecord: Editable): Result
}