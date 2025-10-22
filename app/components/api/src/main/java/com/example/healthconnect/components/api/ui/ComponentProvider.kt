package com.example.healthconnect.components.api.ui

import androidx.compose.runtime.Composable
import com.example.healthconnect.components.api.ui.model.BloodGlucoseLevelEditorModel
import com.example.healthconnect.components.api.ui.model.BodyTemperatureMeasurementLocationEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.PowerEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel
import java.time.Instant
import java.time.ZoneOffset

interface ComponentProvider {

    @Composable
    fun TimeEditor(time: Instant, zoneOffset: ZoneOffset?, onTimeChanged: (TimeEditorModel) -> Unit)

    @Composable
    fun MeasurementLocationSelector(
        bodyTemperatureMeasurementLocationEditorModel: BodyTemperatureMeasurementLocationEditorModel,
        onLocationChanged: (BodyTemperatureMeasurementLocationEditorModel) -> Unit
    )

    @Composable
    fun MetadataEditor(
        metadataEditorModel: MetadataEditorModel,
        onMetadataChanged: (MetadataEditorModel) -> Unit
    )

    @Composable
    fun TemperatureEditor(
        temperatureEditorModel: TemperatureEditorModel,
        onTemperatureChanged: (TemperatureEditorModel) -> Unit
    )

    @Composable
    fun PowerEditor(
        powerEditorModel: PowerEditorModel,
        onPowerChanged: (PowerEditorModel) -> Unit,
    )

    @Composable
    fun BloodGlucoseLevelEditor(
        editorModel: BloodGlucoseLevelEditorModel,
        onChanged:(BloodGlucoseLevelEditorModel) -> Unit,
    )
}