package com.example.healthconnect.editor.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.editor.api.ui.mapper.RecordMapper
import com.example.healthconnect.editor.impl.ui.screen.record.CommonRecordViewModel
import com.example.healthconnect.editor.impl.ui.screen.record.InsertRecordFactory
import com.example.healthconnect.editor.impl.ui.screen.record.InsertRecordViewModel
import com.example.healthconnect.utilty.api.domain.usecase.Insert
import com.example.healthconnect.utilty.api.domain.usecase.Update
import kotlin.reflect.KClass

class RecordViewModelFactory(
    private val recordMapper: RecordMapper,
    private val update: Update,
    private val insertRecordFactory: InsertRecordFactory,
    private val insert: Insert,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T = when (modelClass) {
        CommonRecordViewModel::class -> CommonRecordViewModel(
            initialRecord = checkNotNull(extras[CommonRecordViewModel.RECORD_KEY]),
            recordMapper = recordMapper,
            update = update,
        )

        InsertRecordViewModel::class -> InsertRecordViewModel(
            recordClass = checkNotNull(extras[InsertRecordViewModel.RECORD_CLASS_KEY]),
            insertRecordFactory = insertRecordFactory,
            recordMapper = recordMapper,
            insert = insert,
        )

        else -> throw IllegalStateException("Unknown ViewModel class:" + modelClass.simpleName)
    } as T
}
