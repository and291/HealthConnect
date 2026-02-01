package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.meters
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.components.api.ui.model.top.ValueComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.DistanceModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class DistanceEditor() : Editor<DistanceRecord, DistanceModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: DistanceModel,
        event: ModelModificationEvent,
    ): DistanceModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.Distance -> model.copy(
                distance = event.value
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: DistanceRecord,
        mapper: MetadataMapper,
    ): DistanceModel = DistanceModel(
        time = TimeComponentModel.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        distance = ValueComponentModel.Dbl(
            parsedValue = record.distance.inMeters,
            type = ValueComponentModel.Type.Distance(),
        ),
    )

    override fun toRecord(
        validModel: DistanceModel,
        mapper: MetadataMapper,
    ): DistanceRecord = DistanceRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        distance = (validModel.distance as ValueComponentModel.Dbl).parsedValue!!.meters,
    )

    override fun createDefault(): DistanceRecord {
        val instant = Instant.now()
        return DistanceRecord(
            startTime = instant.minusSeconds(60),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
            distance = 100.0.meters,
        )
    }
}