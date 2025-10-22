package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.BloodGlucoseLevelEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BloodGlucoseLevelRecordEditorModel(
    val timeEditorModel: TimeEditorModel,
    override val metadataEditorModel: MetadataEditorModel,
    val level: BloodGlucoseLevelEditorModel,
) : RecordEditorModel() {

    override fun isValid(): Boolean = timeEditorModel is TimeEditorModel.Valid &&
            level is BloodGlucoseLevelEditorModel.Valid &&
            metadataEditorModel.isValid()

    override fun update(event: RecordEditEvent): RecordEditorModel = when (event) {
        is RecordEditEvent.OnMetadataChanged -> copy(
            metadataEditorModel = event.metadata
        )

        is RecordEditEvent.OnBloodGlucoseLevelChanged -> copy(
            level = event.level
        )

        is RecordEditEvent.OnTimeChanged -> copy(
            timeEditorModel = event.timeEditorModel
        )

        else -> TODO()
    }

}