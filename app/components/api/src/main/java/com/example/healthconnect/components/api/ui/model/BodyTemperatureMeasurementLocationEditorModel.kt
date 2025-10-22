package com.example.healthconnect.components.api.ui.model

sealed class BodyTemperatureMeasurementLocationEditorModel {

    abstract val value: Int

    data class Valid(
        override val value: Int,
    ) : BodyTemperatureMeasurementLocationEditorModel()

    data class Invalid(
        override val value: Int,
    ) : BodyTemperatureMeasurementLocationEditorModel()
}