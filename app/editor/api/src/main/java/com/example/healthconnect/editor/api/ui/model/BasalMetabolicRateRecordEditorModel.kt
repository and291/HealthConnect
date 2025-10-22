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

    override fun update(event: CommonRecordModificationEvent): RecordEditorModel = when (event) {
        is CommonRecordModificationEvent.OnMetaModelChanged -> copy(
            metadataEditorModel = event.metaModel
        )
        is CommonRecordModificationEvent.OnPowerChanged -> copy(
            powerEditorModel = event.powerEditorModel
        )
        is CommonRecordModificationEvent.OnTimeChanged -> copy(
            timeEditorModel = event.timeEditorModel
        )
        else -> TODO()
    }
}