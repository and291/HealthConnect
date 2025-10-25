package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.percent
import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.BodyFatRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class BodyFatEditor() : Editor<BodyFatRecord, BodyFatRecordEditorModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: BodyFatRecordEditorModel,
        event: RecordModificationEvent,
    ): BodyFatRecordEditorModel = when (event) {
        is RecordModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is RecordModificationEvent.OnTimeChanged -> model.copy(
            timeEditorModel = event.time
        )

        is RecordModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueEditorModel.Type.PercentageBodyFat -> model.copy(
                percentage = event.value
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: BodyFatRecord,
        metadataMapper: MetadataMapper,
    ): BodyFatRecordEditorModel = BodyFatRecordEditorModel(
        timeEditorModel = TimeEditorModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = metadataMapper.toEntity(record.metadata),
        percentage = DoubleValueEditorModel.Valid(
            parsedValue = record.percentage.value,
            type = DoubleValueEditorModel.Type.PercentageBodyFat(),
        )
    )

    override fun toRecord(
        validUiModel: BodyFatRecordEditorModel,
        metadataMapper: MetadataMapper,
    ): BodyFatRecord = BodyFatRecord(
        time = (validUiModel.timeEditorModel as TimeEditorModel.Valid).instant,
        zoneOffset = (validUiModel.timeEditorModel as TimeEditorModel.Valid).zoneOffset,
        metadata = metadataMapper.toLibMetadata(validUiModel.metadata),
        percentage = (validUiModel.percentage as DoubleValueEditorModel.Valid).parsedValue.percent,
    )

    override fun createDefault(): BodyFatRecord = BodyFatRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        percentage = 20.percent,
    )
}