package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.percent
import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.BodyFatModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class BodyFatEditor() : Editor<BodyFatRecord, BodyFatModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: BodyFatModel,
        event: ModelModificationEvent,
    ): BodyFatModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.Percentage -> model.copy(
                percentage = event.value
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: BodyFatRecord,
        mapper: MetadataMapper,
    ): BodyFatModel = BodyFatModel(
        time = TimeComponentModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        percentage = ValueComponentModel.ValidDouble(
            parsedValue = record.percentage.value,
            type = ValueComponentModel.Type.Percentage(),
        )
    )

    override fun toRecord(
        validModel: BodyFatModel,
        mapper: MetadataMapper,
    ): BodyFatRecord = BodyFatRecord(
        time = (validModel.time as TimeComponentModel.Valid).instant,
        zoneOffset = (validModel.time as TimeComponentModel.Valid).zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        percentage = (validModel.percentage as ValueComponentModel.ValidDouble).parsedValue.percent,
    )

    override fun createDefault(): BodyFatRecord = BodyFatRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        percentage = 20.percent,
    )
}