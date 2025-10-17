package com.example.healthconnect.components.api.ui

import androidx.compose.runtime.Composable
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel
import java.time.Instant
import java.time.ZoneOffset

interface ComponentProvider {

    @Composable
    fun TimeEditor(time: Instant, zoneOffset: ZoneOffset?, onTimeChanged: (TimeEditorModel) -> Unit)

    @Composable
    fun MeasurementLocationSelector(currentMeasurementLocation: Int, onItemSelected: (Int) -> Unit)

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
}