package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.MenstruationPeriodRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.MenstruationPeriodModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class MenstruationPeriodEditor() : Editor<MenstruationPeriodRecord, MenstruationPeriodModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: MenstruationPeriodModel,
        event: ModelModificationEvent,
    ): MenstruationPeriodModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: MenstruationPeriodRecord,
        mapper: MetadataMapper,
    ): MenstruationPeriodModel = MenstruationPeriodModel(
        time = TimeComponentModel.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
    )

    override fun toRecord(
        validModel: MenstruationPeriodModel,
        mapper: MetadataMapper,
    ): MenstruationPeriodRecord = MenstruationPeriodRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
    )

    override fun createDefault(): MenstruationPeriodRecord {
        val instant = Instant.now()
        return MenstruationPeriodRecord(
            startTime = instant.minusSeconds(3600 * 24 * 3), // 3 days ago
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
        )
    }
}