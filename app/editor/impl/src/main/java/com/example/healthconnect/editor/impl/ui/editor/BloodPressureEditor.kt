package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Pressure
import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.BloodPressureRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class BloodPressureEditor() : Editor<BloodPressureRecord, BloodPressureRecordEditorModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: BloodPressureRecordEditorModel,
        event: RecordModificationEvent,
    ): BloodPressureRecordEditorModel = when (event) {
        is RecordModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is RecordModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is RecordModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueEditorModel.Type.DiastolicPressure -> model.copy(
                diastolic = event.value
            )

            is DoubleValueEditorModel.Type.SystolicPressure -> model.copy(
                systolic = event.value
            )

            else -> throw NotImplementedError()
        }

        is RecordModificationEvent.OnValueSelected -> when (event.selector.type) {
            is SelectorEditorModel.Type.BodyPosition -> model.copy(
                bodyPosition = event.selector
            )

            is SelectorEditorModel.Type.MeasurementLocationBloodPressure -> model.copy(
                measurementLocation = event.selector
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: BloodPressureRecord,
        metadataMapper: MetadataMapper,
    ): BloodPressureRecordEditorModel = BloodPressureRecordEditorModel(
        time = TimeEditorModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = metadataMapper.toEntity(record.metadata),
        systolic = DoubleValueEditorModel.Valid(
            parsedValue = record.systolic.inMillimetersOfMercury,
            type = DoubleValueEditorModel.Type.SystolicPressure()
        ),
        diastolic = DoubleValueEditorModel.Valid(
            parsedValue = record.diastolic.inMillimetersOfMercury,
            type = DoubleValueEditorModel.Type.DiastolicPressure()
        ),
        bodyPosition = SelectorEditorModel.Valid(
            value = record.bodyPosition,
            type = SelectorEditorModel.Type.BodyPosition()
        ),
        measurementLocation = SelectorEditorModel.Valid(
            value = record.measurementLocation,
            type = SelectorEditorModel.Type.MeasurementLocationBloodPressure()
        )
    )

    override fun toRecord(
        validUiModel: BloodPressureRecordEditorModel,
        metadataMapper: MetadataMapper,
    ): BloodPressureRecord = BloodPressureRecord(
        time = (validUiModel.time as TimeEditorModel.Valid).instant,
        zoneOffset = (validUiModel.time as TimeEditorModel.Valid).zoneOffset,
        metadata = metadataMapper.toLibMetadata(validUiModel.metadata),
        systolic = Pressure.Companion.millimetersOfMercury((validUiModel.systolic as DoubleValueEditorModel.Valid).parsedValue),
        diastolic = Pressure.Companion.millimetersOfMercury((validUiModel.diastolic as DoubleValueEditorModel.Valid).parsedValue),
        bodyPosition = (validUiModel.bodyPosition as SelectorEditorModel.Valid).value,
        measurementLocation = (validUiModel.measurementLocation as SelectorEditorModel.Valid).value,
    )

    override fun createDefault(): BloodPressureRecord = BloodPressureRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        systolic = Pressure.Companion.millimetersOfMercury(120.0),
        diastolic = Pressure.Companion.millimetersOfMercury(80.0)
    )
}