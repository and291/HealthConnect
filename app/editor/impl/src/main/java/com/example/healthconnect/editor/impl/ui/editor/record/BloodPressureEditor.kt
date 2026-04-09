package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Pressure
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.models.api.domain.record.BloodPressure
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class BloodPressureEditor() : Editor<BloodPressureRecord, BloodPressure>() {

    override fun toModel(
        record: BloodPressureRecord,
        mapper: MetadataMapper,
    ): BloodPressure = BloodPressure(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset,
            priority = 0,
        ),
        metadata = mapper.toEntity(record.metadata),
        systolic = ValueField.Dbl(
            parsedValue = record.systolic.inMillimetersOfMercury,
            type = ValueField.Type.SystolicPressure(),
            priority = 1,
        ),
        diastolic = ValueField.Dbl(
            parsedValue = record.diastolic.inMillimetersOfMercury,
            type = ValueField.Type.DiastolicPressure(),
            priority = 2,
        ),
        bodyPosition = SelectorField(
            value = record.bodyPosition,
            type = SelectorField.Type.BodyPosition(),
            priority = 3,
        ),
        measurementLocation = SelectorField(
            value = record.measurementLocation,
            type = SelectorField.Type.MeasurementLocationBloodPressure(),
            priority = 4,
        ),
    )

    override fun toRecord(
        validModel: BloodPressure,
        mapper: MetadataMapper,
    ): BloodPressureRecord = BloodPressureRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        systolic = Pressure.millimetersOfMercury((validModel.systolic as ValueField.Dbl).parsedValue!!),
        diastolic = Pressure.millimetersOfMercury((validModel.diastolic as ValueField.Dbl).parsedValue!!),
        bodyPosition = validModel.bodyPosition.value,
        measurementLocation = validModel.measurementLocation.value,
    )

    override fun createDefault(): BloodPressureRecord = BloodPressureRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.unknownRecordingMethod(),
        systolic = Pressure.millimetersOfMercury(120.0),
        diastolic = Pressure.millimetersOfMercury(80.0)
    )
}