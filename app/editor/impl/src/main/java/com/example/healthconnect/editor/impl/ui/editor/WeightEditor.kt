package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.kilograms
import com.example.healthconnect.components.api.ui.model.ValueComponentModel
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

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.Mass -> model.copy(
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
        time = TimeComponentModel.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        weight = ValueComponentModel.Dbl(
            parsedValue = record.weight.inKilograms,
            type = ValueComponentModel.Type.Mass(),
        ),
    )

    override fun toRecord(
        validModel: WeightModel,
        mapper: MetadataMapper,
    ): WeightRecord = WeightRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        weight = (validModel.weight as ValueComponentModel.Dbl).parsedValue!!.kilograms,
    )

    override fun createDefault(): WeightRecord = WeightRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        weight = 60.kilograms,
    )
}