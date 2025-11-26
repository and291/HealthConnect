package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Pressure
import com.example.healthconnect.components.api.ui.model.ValueComponentModel
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

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.DiastolicPressure -> model.copy(
                diastolic = event.value
            )

            is ValueComponentModel.Type.SystolicPressure -> model.copy(
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
        time = TimeComponentModel.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        systolic = ValueComponentModel.Dbl(
            parsedValue = record.systolic.inMillimetersOfMercury,
            type = ValueComponentModel.Type.SystolicPressure()
        ),
        diastolic = ValueComponentModel.Dbl(
            parsedValue = record.diastolic.inMillimetersOfMercury,
            type = ValueComponentModel.Type.DiastolicPressure()
        ),
        bodyPosition = SelectorComponentModel(
            value = record.bodyPosition,
            type = SelectorComponentModel.Type.BodyPosition()
        ),
        measurementLocation = SelectorComponentModel(
            value = record.measurementLocation,
            type = SelectorComponentModel.Type.MeasurementLocationBloodPressure()
        )
    )

    override fun toRecord(
        validModel: BloodPressureModel,
        mapper: MetadataMapper,
    ): BloodPressureRecord = BloodPressureRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        systolic = Pressure.millimetersOfMercury((validModel.systolic as ValueComponentModel.Dbl).parsedValue!!),
        diastolic = Pressure.millimetersOfMercury((validModel.diastolic as ValueComponentModel.Dbl).parsedValue!!),
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