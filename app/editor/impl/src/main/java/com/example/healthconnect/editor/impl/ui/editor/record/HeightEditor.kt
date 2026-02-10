package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.meters
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.domain.record.Height
import java.time.Instant
import java.time.ZoneOffset

class HeightEditor() : Editor<HeightRecord, Height>() {

    override fun toModel(
        record: HeightRecord,
        mapper: MetadataMapper,
    ): Height = Height(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        height = ValueField.Dbl(
            parsedValue = record.height.inMeters,
            type = ValueField.Type.Length(),
        ),
    )

    override fun toRecord(
        validModel: Height,
        mapper: MetadataMapper,
    ): HeightRecord = HeightRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        height = (validModel.height as ValueField.Dbl).parsedValue!!.meters,
    )

    override fun createDefault(): HeightRecord = HeightRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        height = 1.85.meters,
    )
}