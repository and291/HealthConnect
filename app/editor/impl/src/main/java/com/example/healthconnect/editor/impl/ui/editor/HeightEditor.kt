package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.meters
import com.example.healthconnect.components.api.ui.model.top.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
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

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.Length -> model.copy(
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
        time = TimeComponentModel.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        height = ValueComponentModel.Dbl(
            parsedValue = record.height.inMeters,
            type = ValueComponentModel.Type.Length(),
        ),
    )

    override fun toRecord(
        validModel: HeightModel,
        mapper: MetadataMapper,
    ): HeightRecord = HeightRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        height = (validModel.height as ValueComponentModel.Dbl).parsedValue!!.meters,
    )

    override fun createDefault(): HeightRecord = HeightRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        height = 1.85.meters,
    )
}