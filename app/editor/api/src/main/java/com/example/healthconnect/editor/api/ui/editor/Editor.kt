package com.example.healthconnect.editor.api.ui.editor

import androidx.health.connect.client.records.Record
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.RecordEditorModel
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent

sealed class Editor<R : Record, M : RecordEditorModel> {

    abstract fun update(model: M, event: RecordModificationEvent): M

    abstract fun toModel(record: R, metadataMapper: MetadataMapper): M

    abstract fun toRecord(validUiModel: M, metadataMapper: MetadataMapper): R

    abstract fun createDefault(): R
}

