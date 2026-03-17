package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.domain.record.RespiratoryRate
import java.time.Instant
import java.time.ZoneOffset

class RespiratoryRateEditor() : Editor<RespiratoryRateRecord, RespiratoryRate>() {

    override fun toModel(
        record: RespiratoryRateRecord,
        mapper: MetadataMapper,
    ): RespiratoryRate = RespiratoryRate(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset,
            priority = 0
        ),
        metadata = mapper.toEntity(record.metadata),
        rate = ValueField.Dbl(
            parsedValue = record.rate,
            type = ValueField.Type.RespiratoryRate(),
            priority = 1
        )
    )

    override fun toRecord(
        validModel: RespiratoryRate,
        mapper: MetadataMapper,
    ): RespiratoryRateRecord = RespiratoryRateRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        rate = (validModel.rate as ValueField.Dbl).parsedValue!!,
    )

    override fun createDefault(): RespiratoryRateRecord = RespiratoryRateRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        rate = 30.0,
    )
}