package com.example.healthconnect.editor.api.domain.usecase

import com.example.healthconnect.editor.api.domain.entity.Editable
import java.util.UUID
import kotlin.reflect.KClass

interface GetEditable {

    suspend operator fun invoke(uuid: UUID, kClass: KClass<*>): Editable
}

