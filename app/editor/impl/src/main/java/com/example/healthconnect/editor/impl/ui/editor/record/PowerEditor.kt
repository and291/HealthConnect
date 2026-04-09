package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.PowerRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Power
import com.example.healthconnect.components.api.domain.entity.field.atomic.PowerSampleField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.models.api.domain.record.Power as PowerModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class PowerEditor() : Editor<PowerRecord, PowerModel>() {

    override fun toModel(
        record: PowerRecord,
        mapper: MetadataMapper,
    ): PowerModel = PowerModel(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset,
            priority = 0
        ),
        metadata = mapper.toEntity(record.metadata),
        samples = ListField(
            items = record.samples.map {
                PowerSampleField(
                    time = it.time,
                    power = ValueField.Dbl(
                        parsedValue = it.power.inWatts,
                        type = ValueField.Type.PowerWatt(),
                    )
                )
            },
            type = ListField.Type.PowerSamples,
            priority = 1
        )
    )

    override fun toRecord(
        validModel: PowerModel,
        mapper: MetadataMapper,
    ): PowerRecord = PowerRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        samples = validModel.samples.items.map {
            PowerRecord.Sample(
                time = it.time,
                power = Power.watts((it.power as ValueField.Dbl).parsedValue!!)
            )
        }
    )

    override fun createDefault(): PowerRecord {
        val instant = Instant.now()
        return PowerRecord(
            startTime = instant.minusSeconds(3600),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.unknownRecordingMethod(),
            samples = emptyList()
        )
    }
}
