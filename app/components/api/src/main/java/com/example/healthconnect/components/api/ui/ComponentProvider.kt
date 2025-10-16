package com.example.healthconnect.components.api.ui

import androidx.compose.runtime.Composable
import com.example.healthconnect.components.api.domain.entity.metadata.MetadataEntity
import com.example.healthconnect.components.api.ui.model.InstantModel
import java.time.Instant
import java.time.ZoneOffset

interface ComponentProvider {

    @Composable
    fun Time(time: Instant, zoneOffset: ZoneOffset?, onTimeChanged: (InstantModel) -> Unit)

    @Composable
    fun MeasurementLocationSelector(currentMeasurementLocation: Int, onItemSelected: (Int) -> Unit)

    @Composable
    fun MetadataEditor(metadataEntity: MetadataEntity, onMetadataChanged: (MetadataEntity) -> Unit)
}