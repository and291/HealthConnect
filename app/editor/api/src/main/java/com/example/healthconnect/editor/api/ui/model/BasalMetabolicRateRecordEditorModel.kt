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

    override fun update(event: RecordEditEvent): RecordEditorModel = when (event) {
        is RecordEditEvent.OnMetadataChanged -> copy(
            metadataEditorModel = event.metadata
        )

        is RecordEditEvent.OnDoubleValueChanged -> when (event.editorModel.type) {
            is DoubleValueEditorModel.Type.Power -> copy(
                powerEditorModel = event.editorModel
            )

            else -> TODO()
        }

        is RecordEditEvent.OnTimeChanged -> copy(
            timeEditorModel = event.timeEditorModel
        )

        else -> TODO()
    }
}