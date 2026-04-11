package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.OvulationTestRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.models.api.domain.record.OvulationTest
import java.time.Instant
import java.time.ZoneOffset

class OvulationTestEditor() : Editor<OvulationTestRecord, OvulationTest>() {

    override fun toModel(
        record: OvulationTestRecord,
        mapper: MetadataMapper,
    ): OvulationTest = OvulationTest(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset,
            priority = 0
        ),
        metadata = mapper.toEntity(record.metadata),
        result = SelectorField(
            value = record.result,
            type = SelectorField.Type.Result(),
            priority = 1
        ),
    )

    override fun toRecord(
        validModel: OvulationTest,
        mapper: MetadataMapper,
    ): OvulationTestRecord = OvulationTestRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        result = validModel.result.value,
    )

    override fun createDefault(): OvulationTestRecord = OvulationTestRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        result = OvulationTestRecord.RESULT_INCONCLUSIVE,
    )
}