package com.example.healthconnect.utilty.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.editor.api.domain.record.factory.ModelFactory
import com.example.healthconnect.utilty.impl.domain.usecase.Delete
import com.example.healthconnect.utilty.impl.domain.usecase.Read
import com.example.healthconnect.utilty.impl.ui.screen.RecordsViewModel
import com.example.healthconnect.utilty.impl.ui.screen.RecordsViewModel.Companion.RECORD_TYPE_KEY
import kotlin.reflect.KClass

class RecordsViewModelFactory(
    private val read: Read,
    private val delete: Delete,
    private val modelFactory: ModelFactory,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return when (modelClass) {
            RecordsViewModel::class -> RecordsViewModel(
                read = read,
                delete = delete,
                recordType = checkNotNull(extras[RECORD_TYPE_KEY]),
                modelFactory = modelFactory,
            )

            else -> throw IllegalStateException("Unknown ViewModel class:" + modelClass.simpleName)
        } as T
    }
}
