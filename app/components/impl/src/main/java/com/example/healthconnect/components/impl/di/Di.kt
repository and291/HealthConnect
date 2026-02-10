package com.example.healthconnect.components.impl.di

import com.example.healthconnect.components.api.ui.FieldProvider
import com.example.healthconnect.components.impl.ui.FieldProviderImpl
import com.example.healthconnect.components.impl.ui.editor.EditorViewModelFactory
import com.example.healthconnect.components.impl.ui.editor.DeviceTypeMapper
import kotlin.getValue

object Di {

    val fieldProvider: FieldProvider by lazy {
        FieldProviderImpl()
    }

    internal val editorViewModelFactory by lazy {
        EditorViewModelFactory()
    }

    internal val deviceTypeMapper = DeviceTypeMapper()
}