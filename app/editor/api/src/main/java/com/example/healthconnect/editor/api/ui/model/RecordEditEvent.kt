package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.BodyTemperatureMeasurementLocationEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.PowerEditorModel
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

sealed class RecordEditEvent : Event() {

    data class OnTimeChanged(
        val timeEditorModel: TimeEditorModel,
    ) : RecordEditEvent()

    data class OnTemperatureChanged(
        val temperatureEditorModel: TemperatureEditorModel,
    ) : RecordEditEvent()

    data class OnMeasurementLocationSelected(
        val location: BodyTemperatureMeasurementLocationEditorModel
    ) : RecordEditEvent()

    data class OnMetadataChanged(
        val metadata: MetadataEditorModel
    ) : RecordEditEvent()

    data class OnPowerChanged(
        val powerEditorModel: PowerEditorModel,
    ) : RecordEditEvent()
}

sealed class Event {

    data class OnUpdate(
        //https://developer.android.com/health-and-fitness/guides/health-connect/develop/write-data
        val upsert: Boolean = false
    ) : Event()
}