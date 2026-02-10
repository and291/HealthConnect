package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.MenstruationFlowRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.domain.record.MenstruationFlow
import java.time.Instant
import java.time.ZoneOffset

class MenstruationFlowEditor() : Editor<MenstruationFlowRecord, MenstruationFlow>() {

    override fun toModel(
        record: MenstruationFlowRecord,
        mapper: MetadataMapper,
    ): MenstruationFlow = MenstruationFlow(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        flow = SelectorField(
            value = record.flow,
            type = SelectorField.Type.Flow(),
        ),
    )

    override fun toRecord(
        validModel: MenstruationFlow,
        mapper: MetadataMapper,
    ): MenstruationFlowRecord = MenstruationFlowRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        flow = validModel.flow.value,
    )

    override fun createDefault(): MenstruationFlowRecord = MenstruationFlowRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
    )
}