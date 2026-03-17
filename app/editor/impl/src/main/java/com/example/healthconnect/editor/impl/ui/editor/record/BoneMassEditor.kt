package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.BoneMassRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.kilograms
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.editor.api.domain.record.BoneMass
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class BoneMassEditor() : Editor<BoneMassRecord, BoneMass>() {

    override fun toModel(
        record: BoneMassRecord,
        mapper: MetadataMapper,
    ): BoneMass = BoneMass(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset,
            priority = 0
        ),
        metadata = mapper.toEntity(record.metadata),
        mass = ValueField.Dbl(
            parsedValue = record.mass.inKilograms,
            type = ValueField.Type.Mass(),
            priority = 1
        ),
    )

    override fun toRecord(
        validModel: BoneMass,
        mapper: MetadataMapper,
    ): BoneMassRecord = BoneMassRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        mass = (validModel.mass as ValueField.Dbl).parsedValue!!.kilograms,
    )

    override fun createDefault(): BoneMassRecord = BoneMassRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        mass = 5.kilograms,
    )
}