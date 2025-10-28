package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.meters
import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.HeightModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class HeightEditor() : Editor<HeightRecord, HeightModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: HeightModel,
        event: ModelModificationEvent,
    ): HeightModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueComponentModel.Type.Length -> model.copy(
                height = event.value
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: HeightRecord,
        mapper: MetadataMapper,
    ): HeightModel = HeightModel(
        time = TimeComponentModel.Valid(
            instant = record.time, zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        height = DoubleValueComponentModel.Valid(
            parsedValue = record.height.inMeters,
            type = DoubleValueComponentModel.Type.Length(),
        ),
    )

    override fun toRecord(
        validModel: HeightModel,
        mapper: MetadataMapper,
    ): HeightRecord = HeightRecord(
        time = (validModel.time as TimeComponentModel.Valid).instant,
        zoneOffset = (validModel.time as TimeComponentModel.Valid).zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        height = (validModel.height as DoubleValueComponentModel.Valid).parsedValue.meters,
    )

    override fun createDefault(): HeightRecord = HeightRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        height = 1.85.meters,
    )
}