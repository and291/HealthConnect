package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.liters
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.components.api.ui.model.top.ValueComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.HydrationModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class HydrationEditor() : Editor<HydrationRecord, HydrationModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: HydrationModel,
        event: ModelModificationEvent,
    ): HydrationModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.VolumeOfWater -> model.copy(
                volume = event.value
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: HydrationRecord,
        mapper: MetadataMapper,
    ): HydrationModel = HydrationModel(
        time = TimeComponentModel.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        volume = ValueComponentModel.Dbl(
            parsedValue = record.volume.inLiters,
            type = ValueComponentModel.Type.VolumeOfWater(),
        ),
    )

    override fun toRecord(
        validModel: HydrationModel,
        mapper: MetadataMapper,
    ): HydrationRecord = HydrationRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        volume = (validModel.volume as ValueComponentModel.Dbl).parsedValue!!.liters,
    )

    override fun createDefault(): HydrationRecord {
        val instant = Instant.now()
        return HydrationRecord(
            startTime = instant.minusSeconds(60),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
            volume = 0.25.liters,
        )
    }
}