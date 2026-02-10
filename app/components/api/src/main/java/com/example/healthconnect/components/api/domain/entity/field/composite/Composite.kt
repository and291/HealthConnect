package com.example.healthconnect.components.api.domain.entity.field.composite

import com.example.healthconnect.components.api.domain.entity.ComponentModel
import java.util.UUID
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

sealed class Composite(
    override val instanceId: UUID
) : ComponentModel {

    @Suppress("UNCHECKED_CAST")
    fun getComponents(): List<ComponentModel> {
        val kClass = this::class as KClass<Composite>
        return kClass.declaredMemberProperties.mapNotNull { it.get(this) as? ComponentModel }
    }
}