package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.CervicalMucusRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.editor.api.domain.record.CervicalMucus
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class CervicalMucusEditor() : Editor<CervicalMucusRecord, CervicalMucus>() {

    override fun toModel(
        record: CervicalMucusRecord,
        mapper: MetadataMapper,
    ): CervicalMucus = CervicalMucus(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        appearance = SelectorField(
            value = record.appearance,
            type = SelectorField.Type.Appearance(),
        ),
        sensation = SelectorField(
            value = record.sensation,
            type = SelectorField.Type.Sensation(),
        ),
    )

    override fun toRecord(
        validModel: CervicalMucus,
        mapper: MetadataMapper,
    ): CervicalMucusRecord = CervicalMucusRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        appearance = validModel.appearance.value,
        sensation = validModel.sensation.value
    )

    override fun createDefault(): CervicalMucusRecord = CervicalMucusRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
    )
}