package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.ComponentEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

sealed class RecordEditorModel {
    abstract val metadataEditorModel: MetadataEditorModel
    abstract fun isValid(): Boolean
    abstract fun update(event: RecordModificationEvent): RecordEditorModel

    fun getComponents(): List<ComponentEditorModel> {
        val kClass = this::class as KClass<RecordEditorModel>
        return kClass.declaredMemberProperties.map { it.get(this) as ComponentEditorModel }
    }
}