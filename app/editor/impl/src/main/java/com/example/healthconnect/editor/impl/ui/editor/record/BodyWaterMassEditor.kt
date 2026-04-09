package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.BodyWaterMassRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.kilograms
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.models.api.domain.record.BodyWaterMass
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class BodyWaterMassEditor() : Editor<BodyWaterMassRecord, BodyWaterMass>() {

    override fun toModel(
        record: BodyWaterMassRecord,
        mapper: MetadataMapper,
    ): BodyWaterMass = BodyWaterMass(
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
        validModel: BodyWaterMass,
        mapper: MetadataMapper,
    ): BodyWaterMassRecord = BodyWaterMassRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        mass = (validModel.mass as ValueField.Dbl).parsedValue!!.kilograms,
    )

    override fun createDefault(): BodyWaterMassRecord = BodyWaterMassRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        mass = 30.kilograms,
    )
}