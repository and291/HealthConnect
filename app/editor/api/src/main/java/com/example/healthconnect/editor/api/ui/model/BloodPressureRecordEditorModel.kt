package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BloodPressureRecordEditorModel(
    val time: TimeEditorModel,
    override val metadata: MetadataEditorModel,
    val systolic: DoubleValueEditorModel,
    val diastolic: DoubleValueEditorModel,
    val bodyPosition: SelectorEditorModel,
    val measurementLocation: SelectorEditorModel,
) : RecordEditorModel() {
    override fun isValid(): Boolean = time is TimeEditorModel.Valid &&
            systolic is DoubleValueEditorModel.Valid &&
            diastolic is DoubleValueEditorModel.Valid &&
            bodyPosition is SelectorEditorModel.Valid &&
            measurementLocation is SelectorEditorModel.Valid &&
            metadata.isValid()

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(event: RecordModificationEvent): RecordEditorModel = when (event) {
        is RecordModificationEvent.OnMetadataChanged -> copy(
            metadata = event.metadata
        )

        is RecordModificationEvent.OnTimeChanged -> copy(
            time = event.time
        )

        is RecordModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueEditorModel.Type.DiastolicPressure -> copy(
                diastolic = event.value
            )

            is DoubleValueEditorModel.Type.SystolicPressure -> copy(
                systolic = event.value
            )

            else -> throw NotImplementedError()
        }

        is RecordModificationEvent.OnValueSelected -> when (event.selector.type) {
            is SelectorEditorModel.Type.BodyPosition -> copy(
                bodyPosition = event.selector
            )

            is SelectorEditorModel.Type.MeasurementLocationBloodPressure -> copy(
                measurementLocation = event.selector
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

}