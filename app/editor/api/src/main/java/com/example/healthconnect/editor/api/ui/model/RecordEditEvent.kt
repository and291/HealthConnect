package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

sealed class RecordEditEvent : Event() {

    data class OnTimeChanged(
        val timeEditorModel: TimeEditorModel,
    ) : RecordEditEvent()

    data class OnMeasurementLocationSelected(
        val location: SelectorEditorModel
    ) : RecordEditEvent()

    data class OnMetadataChanged(
        val metadata: MetadataEditorModel
    ) : RecordEditEvent()

    data class OnDoubleValueChanged(
        val editorModel: DoubleValueEditorModel,
    ) : RecordEditEvent()
}

sealed class Event {

    data class OnUpdate(
        //https://developer.android.com/health-and-fitness/guides/health-connect/develop/write-data
        val upsert: Boolean = false
    ) : Event()
}