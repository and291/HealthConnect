package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.percent
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.models.api.domain.record.OxygenSaturation
import java.time.Instant
import java.time.ZoneOffset

class OxygenSaturationEditor() : Editor<OxygenSaturationRecord, OxygenSaturation>() {

    override fun toModel(
        record: OxygenSaturationRecord,
        mapper: MetadataMapper,
    ): OxygenSaturation = OxygenSaturation(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset,
            priority = 0
        ),
        metadata = mapper.toEntity(record.metadata),
        percentage = ValueField.Dbl(
            parsedValue = record.percentage.value,
            type = ValueField.Type.Percentage(),
            priority = 1
        )
    )

    override fun toRecord(
        validModel: OxygenSaturation,
        mapper: MetadataMapper,
    ): OxygenSaturationRecord = OxygenSaturationRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        percentage = (validModel.percentage as ValueField.Dbl).parsedValue!!.percent,
    )

    override fun createDefault(): OxygenSaturationRecord = OxygenSaturationRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        percentage = 20.percent,
    )
}