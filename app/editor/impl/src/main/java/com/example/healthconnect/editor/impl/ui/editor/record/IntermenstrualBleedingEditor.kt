package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.IntermenstrualBleedingRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.models.api.domain.record.IntermenstrualBleeding
import java.time.Instant
import java.time.ZoneOffset

class IntermenstrualBleedingEditor() :
    Editor<IntermenstrualBleedingRecord, IntermenstrualBleeding>() {

    override fun toModel(
        record: IntermenstrualBleedingRecord,
        mapper: MetadataMapper,
    ): IntermenstrualBleeding = IntermenstrualBleeding(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset,
            priority = 0
        ),
        metadata = mapper.toEntity(record.metadata),
    )

    override fun toRecord(
        validModel: IntermenstrualBleeding,
        mapper: MetadataMapper,
    ): IntermenstrualBleedingRecord = IntermenstrualBleedingRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
    )

    override fun createDefault(): IntermenstrualBleedingRecord = IntermenstrualBleedingRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
    )
}