package com.example.healthconnect.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.domain.usecase.Read
import com.example.healthconnect.ui.screen.RecordsViewModel
import com.example.healthconnect.ui.screen.RecordsViewModel.Companion.RECORD_TYPE_KEY
import kotlin.reflect.KClass

class RecordsViewModelFactory(
    private val read: Read,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return when (modelClass) {
            RecordsViewModel::class -> RecordsViewModel(
                read = read,
                recordType = checkNotNull(extras[RECORD_TYPE_KEY])
            )

            else -> throw IllegalStateException("Unknown ViewModel class:" + modelClass.simpleName)
        } as T
    }
}
