package com.example.healthconnect.editor.api.domain.usecase

import com.example.healthconnect.editor.api.domain.entity.Editable
import kotlin.reflect.KClass

interface CreateEditable {

    operator fun invoke(klass: KClass<*>): Editable
}

