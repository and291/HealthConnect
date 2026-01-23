package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.ElevationGainedRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.meters
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.ElevationGainedModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class ElevationGainedEditor() : Editor<ElevationGainedRecord, ElevationGainedModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: ElevationGainedModel,
        event: ModelModificationEvent,
    ): ElevationGainedModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.Elevation -> model.copy(
                elevation = event.value
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: ElevationGainedRecord,
        mapper: MetadataMapper,
    ): ElevationGainedModel = ElevationGainedModel(
        time = TimeComponentModel.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        elevation = ValueComponentModel.Dbl(
            parsedValue = record.elevation.inMeters,
            type = ValueComponentModel.Type.Elevation(),
        ),
    )

    override fun toRecord(
        validModel: ElevationGainedModel,
        mapper: MetadataMapper,
    ): ElevationGainedRecord = ElevationGainedRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        elevation = (validModel.elevation as ValueComponentModel.Dbl).parsedValue!!.meters,
    )

    override fun createDefault(): ElevationGainedRecord {
        val instant = Instant.now()
        return ElevationGainedRecord(
            startTime = instant.minusSeconds(60),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
            elevation = 10.0.meters,
        )
    }
}