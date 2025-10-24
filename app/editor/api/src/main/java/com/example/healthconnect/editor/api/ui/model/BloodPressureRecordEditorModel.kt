package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BloodPressureRecordEditorModel(
    val timeEditorModel: TimeEditorModel,
    override val metadataEditorModel: MetadataEditorModel,
    val systolic: DoubleValueEditorModel,
    val diastolic: DoubleValueEditorModel,
    val bodyPosition: SelectorEditorModel,
    val measurementLocation: SelectorEditorModel,
) : RecordEditorModel() {
    override fun isValid(): Boolean = timeEditorModel is TimeEditorModel.Valid &&
            systolic is DoubleValueEditorModel.Valid &&
            diastolic is DoubleValueEditorModel.Valid &&
            bodyPosition is SelectorEditorModel.Valid &&
            measurementLocation is SelectorEditorModel.Valid &&
            metadataEditorModel.isValid()

    override fun update(event: RecordModificationEvent): RecordEditorModel = when (event) {
        is RecordModificationEvent.OnMetadataChanged -> copy(
            metadataEditorModel = event.metadata
        )
        is RecordModificationEvent.OnTimeChanged -> copy(
            timeEditorModel = event.timeEditorModel
        )
        is RecordModificationEvent.OnDoubleValueChanged -> when (event.editorModel.type) {
            is DoubleValueEditorModel.Type.DiastolicPressure -> copy(
                diastolic = event.editorModel
            )
            is DoubleValueEditorModel.Type.SystolicPressure -> copy(
                systolic = event.editorModel
            )
            else -> TODO()
        }
        is RecordModificationEvent.OnValueSelected -> when (event.editorModel.type) {
            is SelectorEditorModel.Type.BodyPosition -> copy(
                bodyPosition = event.editorModel
            )
            is SelectorEditorModel.Type.MeasurementLocationBloodPressure -> copy(
                measurementLocation = event.editorModel
            )
            else -> TODO()
        }
    }

}