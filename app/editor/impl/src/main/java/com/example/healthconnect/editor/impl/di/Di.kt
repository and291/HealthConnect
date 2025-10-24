package com.example.healthconnect.editor.impl.di

import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.editor.api.ui.mapper.DeviceMapper
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.mapper.RecordMapper
import com.example.healthconnect.editor.impl.ui.RecordViewModelFactory
import com.example.healthconnect.editor.impl.ui.screen.record.ComponentFactory
import com.example.healthconnect.editor.impl.ui.screen.record.InsertRecordFactory
import com.example.healthconnect.utilty.api.domain.usecase.Insert
import com.example.healthconnect.utilty.api.domain.usecase.Update

object Di {

    lateinit var componentProvider: ComponentProvider
    lateinit var update: Update
    lateinit var insert: Insert

    val componentFactory by lazy {
        ComponentFactory(
            componentProvider = componentProvider
        )
    }

    val recordMapper: RecordMapper = RecordMapper(
        metadataMapper = MetadataMapper(
            deviceMapper = DeviceMapper()
        )
    )

    val recordViewModelFactory: RecordViewModelFactory by lazy {
        RecordViewModelFactory(
            recordMapper = recordMapper,
            update = update,
            insertRecordFactory = insertRecordFactory,
            insert = insert,
        )
    }

    val insertRecordFactory = InsertRecordFactory()
}