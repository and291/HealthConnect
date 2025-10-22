package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.PowerEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BasalMetabolicRateRecordEditorModel(
    val timeEditorModel: TimeEditorModel,
    val powerEditorModel: PowerEditorModel,
    override val metadataEditorModel: MetadataEditorModel,
) : RecordEditorModel() {

    override fun isValid(): Boolean = timeEditorModel is TimeEditorModel.Valid &&
            powerEditorModel is PowerEditorModel.Valid &&
            metadataEditorModel.isValid()

    override fun update(event: RecordEditEvent): RecordEditorModel = when (event) {
        is RecordEditEvent.OnMetadataChanged -> copy(
            metadataEditorModel = event.metadata
        )
        is RecordEditEvent.OnPowerChanged -> copy(
            powerEditorModel = event.powerEditorModel
        )
        is RecordEditEvent.OnTimeChanged -> copy(
            timeEditorModel = event.timeEditorModel
        )
        else -> TODO()
    }
}