package com.example.healthconnect.editor.api.ui.editor

import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Power
import androidx.health.connect.client.units.kilocaloriesPerDay
import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.BasalMetabolicRateRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class BasalMetabolicRateEditor() :
    Editor<BasalMetabolicRateRecord, BasalMetabolicRateRecordEditorModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: BasalMetabolicRateRecordEditorModel,
        event: RecordModificationEvent,
    ): BasalMetabolicRateRecordEditorModel = when (event) {
        is RecordModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is RecordModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueEditorModel.Type.Power -> model.copy(
                power = event.value
            )

            else -> throw NotImplementedError()
        }

        is RecordModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: BasalMetabolicRateRecord,
        metadataMapper: MetadataMapper,
    ): BasalMetabolicRateRecordEditorModel = BasalMetabolicRateRecordEditorModel(
        time = TimeEditorModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = metadataMapper.toEntity(record.metadata),
        power = DoubleValueEditorModel.Valid(
            parsedValue = record.basalMetabolicRate.inKilocaloriesPerDay,
            type = DoubleValueEditorModel.Type.Power(),
        )
    )

    override fun toRecord(
        validUiModel: BasalMetabolicRateRecordEditorModel,
        metadataMapper: MetadataMapper,
    ): BasalMetabolicRateRecord = BasalMetabolicRateRecord(
        time = (validUiModel.time as TimeEditorModel.Valid).instant,
        zoneOffset = validUiModel.time.zoneOffset,
        metadata = metadataMapper.toLibMetadata(validUiModel.metadata),
        basalMetabolicRate = (validUiModel.power as DoubleValueEditorModel.Valid).parsedValue.kilocaloriesPerDay,
    )

    override fun createDefault(): BasalMetabolicRateRecord = BasalMetabolicRateRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        basalMetabolicRate = Power.Companion.kilocaloriesPerDay(2500.0),
    )
}