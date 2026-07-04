package com.example.healthconnect.editor.api.domain.usecase

import com.example.healthconnect.editor.api.domain.entity.Editable
import com.example.healthconnect.editor.api.domain.entity.Result

interface Insert {

    operator fun invoke(editable: Editable): Result
}