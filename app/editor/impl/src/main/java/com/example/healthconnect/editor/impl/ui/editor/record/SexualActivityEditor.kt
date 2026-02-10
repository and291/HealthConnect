package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.SexualActivityRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.domain.record.SexualActivity
import java.time.Instant
import java.time.ZoneOffset

class SexualActivityEditor() : Editor<SexualActivityRecord, SexualActivity>() {

    override fun toModel(
        record: SexualActivityRecord,
        mapper: MetadataMapper,
    ): SexualActivity = SexualActivity(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        protectionUsed = SelectorField(
            value = record.protectionUsed,
            type = SelectorField.Type.ProtectionUsed(),
        ),
    )

    override fun toRecord(
        validModel: SexualActivity,
        mapper: MetadataMapper,
    ): SexualActivityRecord = SexualActivityRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        protectionUsed = validModel.protectionUsed.value,
    )

    override fun createDefault(): SexualActivityRecord = SexualActivityRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
    )
}