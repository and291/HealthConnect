package com.example.healthconnect.editor.api.domain.record

import com.example.healthconnect.components.api.domain.entity.Field
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

sealed class Model {

    abstract val metadata: MetadataField

    fun isValid(): Boolean = getComponents().all { it.isValid() }

    @Suppress("UNCHECKED_CAST")
    fun getComponents(): List<Field> {
        val kClass = this::class as KClass<Model>
        return kClass.declaredMemberProperties.map { it.get(this) as Field }
    }
}