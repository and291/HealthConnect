package com.example.healthconnect.components.api.ui

import androidx.compose.runtime.Composable
import com.example.healthconnect.components.api.ui.model.MetadataModel
import com.example.healthconnect.components.api.ui.model.InstantModel
import com.example.healthconnect.components.api.ui.model.TemperatureModel
import java.time.Instant
import java.time.ZoneOffset

interface ComponentProvider {

    @Composable
    fun Time(time: Instant, zoneOffset: ZoneOffset?, onTimeChanged: (InstantModel) -> Unit)

    @Composable
    fun MeasurementLocationSelector(currentMeasurementLocation: Int, onItemSelected: (Int) -> Unit)

    @Composable
    fun MetadataEditor(metadataModel: MetadataModel, onMetadataChanged: (MetadataModel) -> Unit)

    @Composable
    fun Temperature(
        temperatureModel: TemperatureModel,
        onTemperatureChanged: (TemperatureModel) -> Unit
    )
}