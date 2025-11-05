package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.IntermenstrualBleedingRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.IntermenstrualBleedingModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class IntermenstrualBleedingEditor() : Editor<IntermenstrualBleedingRecord, IntermenstrualBleedingModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: IntermenstrualBleedingModel,
        event: ModelModificationEvent,
    ): IntermenstrualBleedingModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueChanged -> throw NotImplementedError()

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: IntermenstrualBleedingRecord,
        mapper: MetadataMapper,
    ): IntermenstrualBleedingModel = IntermenstrualBleedingModel(
        time = TimeComponentModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
    )

    override fun toRecord(
        validModel: IntermenstrualBleedingModel,
        mapper: MetadataMapper,
    ): IntermenstrualBleedingRecord = IntermenstrualBleedingRecord(
        time = (validModel.time as TimeComponentModel.Valid).instant,
        zoneOffset = (validModel.time as TimeComponentModel.Valid).zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
    )

    override fun createDefault(): IntermenstrualBleedingRecord = IntermenstrualBleedingRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
    )
}