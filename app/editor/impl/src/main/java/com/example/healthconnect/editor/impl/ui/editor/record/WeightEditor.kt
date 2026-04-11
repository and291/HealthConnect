package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.kilograms
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.models.api.domain.record.Weight
import java.time.Instant
import java.time.ZoneOffset

class WeightEditor() : Editor<WeightRecord, Weight>() {

    override fun toModel(
        record: WeightRecord,
        mapper: MetadataMapper,
    ): Weight = Weight(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset,
            priority = 0
        ),
        metadata = mapper.toEntity(record.metadata),
        weight = ValueField.Dbl(
            parsedValue = record.weight.inKilograms,
            type = ValueField.Type.Mass(),
            priority = 1
        ),
    )

    override fun toRecord(
        validModel: Weight,
        mapper: MetadataMapper,
    ): WeightRecord = WeightRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        weight = (validModel.weight as ValueField.Dbl).parsedValue!!.kilograms,
    )

    override fun createDefault(): WeightRecord = WeightRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        weight = 60.kilograms,
    )
}