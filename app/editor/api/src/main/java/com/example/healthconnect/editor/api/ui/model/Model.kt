package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.ComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

sealed class Model {

    abstract val metadata: MetadataComponentModel

    abstract fun isValid(): Boolean

    fun getComponents(): List<ComponentModel> {
        val kClass = this::class as KClass<Model>
        return kClass.declaredMemberProperties.map { it.get(this) as ComponentModel }
    }
}