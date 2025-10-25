package com.example.healthconnect.editor.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.editor.api.ui.editor.EditorFactory
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.impl.ui.screen.record.EditRecordViewModel
import com.example.healthconnect.editor.impl.ui.screen.record.InsertRecordViewModel
import com.example.healthconnect.utilty.api.domain.usecase.Insert
import com.example.healthconnect.utilty.api.domain.usecase.Update
import kotlin.reflect.KClass

class RecordViewModelFactory(
    private val editorFactory: EditorFactory,
    private val metadataMapper: MetadataMapper,
    private val update: Update,
    private val insert: Insert,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T = when (modelClass) {
        EditRecordViewModel::class -> EditRecordViewModel(
            initialRecord = checkNotNull(extras[EditRecordViewModel.RECORD_KEY]),
            editorFactory = editorFactory,
            metadataMapper = metadataMapper,
            update = update,
        )

        InsertRecordViewModel::class -> InsertRecordViewModel(
            recordClass = checkNotNull(extras[InsertRecordViewModel.RECORD_CLASS_KEY]),
            editorFactory = editorFactory,
            metadataMapper = metadataMapper,
            insert = insert,
        )

        else -> throw IllegalStateException("Unknown ViewModel class:" + modelClass.simpleName)
    } as T
}
