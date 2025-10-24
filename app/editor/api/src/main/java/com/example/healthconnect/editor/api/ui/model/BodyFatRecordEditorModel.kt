package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BodyFatRecordEditorModel(
    val timeEditorModel: TimeEditorModel,
    override val metadataEditorModel: MetadataEditorModel,
    val percentage: DoubleValueEditorModel,
) : RecordEditorModel() {
    override fun isValid(): Boolean = timeEditorModel is TimeEditorModel.Valid &&
            percentage is DoubleValueEditorModel.Valid &&
            metadataEditorModel.isValid()

    override fun update(event: RecordModificationEvent): RecordEditorModel = when (event) {
        is RecordModificationEvent.OnMetadataChanged -> copy(
            metadataEditorModel = event.metadata
        )
        is RecordModificationEvent.OnTimeChanged -> copy(
            timeEditorModel = event.timeEditorModel
        )
        is RecordModificationEvent.OnDoubleValueChanged -> when (event.editorModel.type) {
            is DoubleValueEditorModel.Type.PercentageBodyFat -> copy(
                percentage = event.editorModel
            )
            else -> throw NotImplementedError()
        }
        is RecordModificationEvent.OnValueSelected -> throw NotImplementedError()
    }
}