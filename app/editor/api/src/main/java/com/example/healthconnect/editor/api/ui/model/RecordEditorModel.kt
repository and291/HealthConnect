package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.MetadataEditorModel

sealed class RecordEditorModel {
    abstract val metadataEditorModel: MetadataEditorModel
    abstract fun isValid(): Boolean
    abstract fun update(event: CommonRecordModificationEvent): RecordEditorModel
}