package com.example.healthconnect.editor.impl.di

import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.editor.api.ui.mapper.DeviceMapper
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.impl.ui.editor.EditorFactory
import com.example.healthconnect.editor.impl.ui.RecordViewModelFactory
import com.example.healthconnect.editor.impl.ui.screen.record.ComponentFactory
import com.example.healthconnect.utilty.api.domain.usecase.Insert
import com.example.healthconnect.utilty.api.domain.usecase.Update

object Di {

    lateinit var componentProvider: ComponentProvider
    lateinit var update: Update
    lateinit var insert: Insert

    val componentFactory by lazy {
        ComponentFactory(
            provider = componentProvider
        )
    }

    val recordViewModelFactory: RecordViewModelFactory by lazy {
        RecordViewModelFactory(
            editorFactory = editorFactory,
            metadataMapper = metadataMapper,
            update = update,
            insert = insert,
        )
    }

    private val editorFactory = EditorFactory()

    private val metadataMapper = MetadataMapper(
        deviceMapper = DeviceMapper()
    )
}