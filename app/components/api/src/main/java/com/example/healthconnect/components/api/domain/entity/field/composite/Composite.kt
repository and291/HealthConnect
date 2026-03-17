package com.example.healthconnect.components.api.domain.entity.field.composite

import com.example.healthconnect.components.api.domain.entity.Field
import java.util.UUID
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

sealed class Composite(
    override val instanceId: UUID
) : Field {

    @Suppress("UNCHECKED_CAST")
    fun getComponents(): List<Field> {
        val kClass = this::class as KClass<Composite>
        return kClass.declaredMemberProperties.mapNotNull { it.get(this) as? Field }
    }

    abstract fun containsInstanceId(instanceId: UUID): Boolean

    abstract fun updateFieldByInstanceId(instanceId: UUID, newField: Field): Field
}