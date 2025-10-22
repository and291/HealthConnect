package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.BodyTemperatureMeasurementLocationEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.PowerEditorModel
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

sealed class CommonRecordModificationEvent : Event() {

    data class OnTimeChanged(
        val timeEditorModel: TimeEditorModel,
    ) : CommonRecordModificationEvent()

    data class OnTemperatureChanged(
        val temperatureEditorModel: TemperatureEditorModel,
    ) : CommonRecordModificationEvent()

    data class OnMeasurementLocationSelected(
        val location: BodyTemperatureMeasurementLocationEditorModel
    ) : CommonRecordModificationEvent()

    data class OnMetaModelChanged(
        val metaModel: MetadataEditorModel
    ) : CommonRecordModificationEvent()

    data class OnPowerChanged(
        val powerEditorModel: PowerEditorModel,
    ) : CommonRecordModificationEvent()
}

sealed class Event {

    data class OnSave(
        //https://developer.android.com/health-and-fitness/guides/health-connect/develop/write-data
        val upsert: Boolean = false
    ) : Event()
}