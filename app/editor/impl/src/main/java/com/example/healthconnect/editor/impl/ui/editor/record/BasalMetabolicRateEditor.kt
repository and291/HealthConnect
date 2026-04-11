package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Power
import androidx.health.connect.client.units.kilocaloriesPerDay
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.models.api.domain.record.BasalMetabolicRate
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class BasalMetabolicRateEditor() :
    Editor<BasalMetabolicRateRecord, BasalMetabolicRate>() {

    override fun toModel(
        record: BasalMetabolicRateRecord,
        mapper: MetadataMapper,
    ): BasalMetabolicRate =
        BasalMetabolicRate(
            time = TimeField.Instantaneous(
                instant = record.time,
                zoneOffset = record.zoneOffset,
                priority = 0
            ),
            metadata = mapper.toEntity(record.metadata),
            power = ValueField.Dbl(
                parsedValue = record.basalMetabolicRate.inKilocaloriesPerDay,
                type = ValueField.Type.Power(),
                priority = 1
            )
        )

    override fun toRecord(
        validModel: BasalMetabolicRate,
        mapper: MetadataMapper,
    ): BasalMetabolicRateRecord = BasalMetabolicRateRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        basalMetabolicRate = (validModel.power as ValueField.Dbl).parsedValue!!.kilocaloriesPerDay,
    )

    override fun createDefault(): BasalMetabolicRateRecord = BasalMetabolicRateRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        basalMetabolicRate = Power.kilocaloriesPerDay(2500.0),
    )
}