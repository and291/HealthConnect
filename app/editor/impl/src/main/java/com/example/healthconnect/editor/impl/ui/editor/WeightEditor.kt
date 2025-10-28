package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.kilograms
import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.WeightModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class WeightEditor() : Editor<WeightRecord, WeightModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: WeightModel,
        event: ModelModificationEvent,
    ): WeightModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueComponentModel.Type.Mass -> model.copy(
                weight = event.value
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: WeightRecord,
        mapper: MetadataMapper,
    ): WeightModel = WeightModel(
        time = TimeComponentModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        weight = DoubleValueComponentModel.Valid(
            parsedValue = record.weight.inKilograms,
            type = DoubleValueComponentModel.Type.Mass(),
        ),
    )

    override fun toRecord(
        validModel: WeightModel,
        mapper: MetadataMapper,
    ): WeightRecord = WeightRecord(
        time = (validModel.time as TimeComponentModel.Valid).instant,
        zoneOffset = (validModel.time as TimeComponentModel.Valid).zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        weight = (validModel.weight as DoubleValueComponentModel.Valid).parsedValue.kilograms,
    )

    override fun createDefault(): WeightRecord = WeightRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        weight = 60.kilograms,
    )
}