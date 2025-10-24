package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BasalMetabolicRateRecordEditorModel(
    val time: TimeEditorModel,
    val power: DoubleValueEditorModel,
    override val metadata: MetadataEditorModel,
) : RecordEditorModel() {

    override fun isValid(): Boolean = time is TimeEditorModel.Valid &&
            power is DoubleValueEditorModel.Valid &&
            metadata.isValid()

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(event: RecordModificationEvent): RecordEditorModel = when (event) {
        is RecordModificationEvent.OnMetadataChanged -> copy(
            metadata = event.metadata
        )

        is RecordModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueEditorModel.Type.Power -> copy(
                power = event.value
            )

            else -> throw NotImplementedError()
        }

        is RecordModificationEvent.OnTimeChanged -> copy(
            time = event.time
        )

        else -> throw NotImplementedError()
    }
}