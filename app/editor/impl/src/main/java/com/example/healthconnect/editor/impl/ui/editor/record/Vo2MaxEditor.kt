package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.Vo2MaxRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.domain.record.Vo2Max
import java.time.Instant
import java.time.ZoneOffset

class Vo2MaxEditor() : Editor<Vo2MaxRecord, Vo2Max>() {

    override fun toModel(
        record: Vo2MaxRecord,
        mapper: MetadataMapper,
    ): Vo2Max = Vo2Max(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        vo2MillilitersPerMinuteKilogram = ValueField.Dbl(
            parsedValue = record.vo2MillilitersPerMinuteKilogram,
            type = ValueField.Type.Vo2Max(),
        ),
        measurementMethod = SelectorField(
            value = record.measurementMethod,
            type = SelectorField.Type.Vo2MaxMeasurementMethod(),
        ),
    )

    override fun toRecord(
        validModel: Vo2Max,
        mapper: MetadataMapper,
    ): Vo2MaxRecord = Vo2MaxRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        vo2MillilitersPerMinuteKilogram = (validModel.vo2MillilitersPerMinuteKilogram as ValueField.Dbl).parsedValue!!,
        measurementMethod = validModel.measurementMethod.value,
    )

    override fun createDefault(): Vo2MaxRecord = Vo2MaxRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        vo2MillilitersPerMinuteKilogram = 50.0,
    )
}