package com.example.healthconnect.utilty.impl.ui.screen.record.model

import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.PowerEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BasalMetabolicRateEditorModel(
    val timeEditorModel: TimeEditorModel,
    val powerEditorModel: PowerEditorModel,
    override val metadataEditorModel: MetadataEditorModel,
) : EditorModel() {

    fun isValid(): Boolean = timeEditorModel is TimeEditorModel.Valid &&
            powerEditorModel is PowerEditorModel.Valid &&
            metadataEditorModel.isValid()
}