package com.example.healthconnect.editor.impl.ui.editor.record.factory

import androidx.health.connect.client.records.Record
import com.example.healthconnect.editor.api.domain.record.Model
import com.example.healthconnect.editor.api.domain.record.factory.RecordFactory
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.impl.ui.editor.EditorFactory

class RecordFactoryImpl(
    private val metadataMapper: MetadataMapper,
    private val editorFactory: EditorFactory,
) : RecordFactory {

    override fun create(record: Record): Model =
        editorFactory.create(record::class).toModel(record, metadataMapper)
}