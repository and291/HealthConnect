package com.example.healthconnect.editor.impl.di

import com.example.healthconnect.editor.api.domain.record.factory.ModelFactory
import com.example.healthconnect.editor.api.ui.mapper.DeviceMapper
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.impl.ui.editor.EditorFactory
import com.example.healthconnect.editor.impl.ui.editor.record.factory.ModelFactoryImpl

object Di {

    val modelFactory: ModelFactory by lazy {
        ModelFactoryImpl(metadataMapper, editorFactory)
    }

    val editorFactory = EditorFactory()

    private val metadataMapper = MetadataMapper(
        deviceMapper = DeviceMapper()
    )
}