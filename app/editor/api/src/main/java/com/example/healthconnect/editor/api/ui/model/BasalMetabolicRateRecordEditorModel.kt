package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BasalMetabolicRateRecordEditorModel(
    val timeEditorModel: TimeEditorModel,
    val powerEditorModel: DoubleValueEditorModel,
    override val metadataEditorModel: MetadataEditorModel,
) : RecordEditorModel() {

    override fun isValid(): Boolean = timeEditorModel is TimeEditorModel.Valid &&
            powerEditorModel is DoubleValueEditorModel.Valid &&
            metadataEditorModel.isValid()

    override fun update(event: RecordModificationEvent): RecordEditorModel = when (event) {
        is RecordModificationEvent.OnMetadataChanged -> copy(
            metadataEditorModel = event.metadata
        )

        is RecordModificationEvent.OnDoubleValueChanged -> when (event.editorModel.type) {
            is DoubleValueEditorModel.Type.Power -> copy(
                powerEditorModel = event.editorModel
            )

            else -> TODO()
        }

        is RecordModificationEvent.OnTimeChanged -> copy(
            timeEditorModel = event.timeEditorModel
        )

        else -> TODO()
    }
}