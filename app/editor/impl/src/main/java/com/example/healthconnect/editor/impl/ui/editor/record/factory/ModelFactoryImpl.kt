package com.example.healthconnect.editor.impl.ui.editor.record.factory

import androidx.health.connect.client.records.Record
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.editor.api.domain.record.factory.ModelFactory
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.impl.ui.editor.EditorFactory

class ModelFactoryImpl(
    private val metadataMapper: MetadataMapper,
    private val editorFactory: EditorFactory,
) : ModelFactory {

    override fun create(record: Record): Model =
        editorFactory.createByRecord(record::class).toModel(record, metadataMapper)

    override fun createByModel(model: Model): Record =
        editorFactory.createByModel(model::class).toRecord(model, metadataMapper)
}