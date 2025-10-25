package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Pressure
import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.BloodPressureModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class BloodPressureEditor() : Editor<BloodPressureRecord, BloodPressureModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: BloodPressureModel,
        event: ModelModificationEvent,
    ): BloodPressureModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueComponentModel.Type.DiastolicPressure -> model.copy(
                diastolic = event.value
            )

            is DoubleValueComponentModel.Type.SystolicPressure -> model.copy(
                systolic = event.value
            )

            else -> throw NotImplementedError()
        }

        is ModelModificationEvent.OnValueSelected -> when (event.selector.type) {
            is SelectorComponentModel.Type.BodyPosition -> model.copy(
                bodyPosition = event.selector
            )

            is SelectorComponentModel.Type.MeasurementLocationBloodPressure -> model.copy(
                measurementLocation = event.selector
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: BloodPressureRecord,
        mapper: MetadataMapper,
    ): BloodPressureModel = BloodPressureModel(
        time = TimeComponentModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        systolic = DoubleValueComponentModel.Valid(
            parsedValue = record.systolic.inMillimetersOfMercury,
            type = DoubleValueComponentModel.Type.SystolicPressure()
        ),
        diastolic = DoubleValueComponentModel.Valid(
            parsedValue = record.diastolic.inMillimetersOfMercury,
            type = DoubleValueComponentModel.Type.DiastolicPressure()
        ),
        bodyPosition = SelectorComponentModel.Valid(
            value = record.bodyPosition,
            type = SelectorComponentModel.Type.BodyPosition()
        ),
        measurementLocation = SelectorComponentModel.Valid(
            value = record.measurementLocation,
            type = SelectorComponentModel.Type.MeasurementLocationBloodPressure()
        )
    )

    override fun toRecord(
        validModel: BloodPressureModel,
        mapper: MetadataMapper,
    ): BloodPressureRecord = BloodPressureRecord(
        time = (validModel.time as TimeComponentModel.Valid).instant,
        zoneOffset = (validModel.time as TimeComponentModel.Valid).zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        systolic = Pressure.Companion.millimetersOfMercury((validModel.systolic as DoubleValueComponentModel.Valid).parsedValue),
        diastolic = Pressure.Companion.millimetersOfMercury((validModel.diastolic as DoubleValueComponentModel.Valid).parsedValue),
        bodyPosition = (validModel.bodyPosition as SelectorComponentModel.Valid).value,
        measurementLocation = (validModel.measurementLocation as SelectorComponentModel.Valid).value,
    )

    override fun createDefault(): BloodPressureRecord = BloodPressureRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        systolic = Pressure.Companion.millimetersOfMercury(120.0),
        diastolic = Pressure.Companion.millimetersOfMercury(80.0)
    )
}