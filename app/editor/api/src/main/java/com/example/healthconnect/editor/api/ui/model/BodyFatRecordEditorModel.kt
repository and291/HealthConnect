package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BodyFatRecordEditorModel(
    val timeEditorModel: TimeEditorModel,
    override val metadata: MetadataEditorModel,
    val percentage: DoubleValueEditorModel,
) : RecordEditorModel() {
    override fun isValid(): Boolean = timeEditorModel is TimeEditorModel.Valid &&
            percentage is DoubleValueEditorModel.Valid &&
            metadata.isValid()

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(event: RecordModificationEvent): RecordEditorModel = when (event) {
        is RecordModificationEvent.OnMetadataChanged -> copy(
            metadata = event.metadata
        )

        is RecordModificationEvent.OnTimeChanged -> copy(
            timeEditorModel = event.time
        )

        is RecordModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueEditorModel.Type.PercentageBodyFat -> copy(
                percentage = event.value
            )

            else -> throw NotImplementedError()
        }

        is RecordModificationEvent.OnValueSelected -> throw NotImplementedError()
        else -> throw NotImplementedError()
    }
}