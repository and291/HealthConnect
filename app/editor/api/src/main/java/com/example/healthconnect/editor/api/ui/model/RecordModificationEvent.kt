package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

sealed class RecordModificationEvent {

    data class OnTimeChanged(
        val time: TimeEditorModel,
    ) : RecordModificationEvent()

    data class OnValueSelected(
        val selector: SelectorEditorModel
    ) : RecordModificationEvent()

    data class OnMetadataChanged(
        val metadata: MetadataEditorModel
    ) : RecordModificationEvent()

    data class OnDoubleValueChanged(
        val value: DoubleValueEditorModel,
    ) : RecordModificationEvent()
}