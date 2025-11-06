package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.BoneMassRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.kilograms
import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.BoneMassModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class BoneMassEditor() : Editor<BoneMassRecord, BoneMassModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: BoneMassModel,
        event: ModelModificationEvent,
    ): BoneMassModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.Mass -> model.copy(
                mass = event.value
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: BoneMassRecord,
        mapper: MetadataMapper,
    ): BoneMassModel = BoneMassModel(
        time = TimeComponentModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        mass = ValueComponentModel.ValidDouble(
            parsedValue = record.mass.inKilograms,
            type = ValueComponentModel.Type.Mass(),
        ),
    )

    override fun toRecord(
        validModel: BoneMassModel,
        mapper: MetadataMapper,
    ): BoneMassRecord = BoneMassRecord(
        time = (validModel.time as TimeComponentModel.Valid).instant,
        zoneOffset = (validModel.time as TimeComponentModel.Valid).zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        mass = (validModel.mass as ValueComponentModel.ValidDouble).parsedValue.kilograms,
    )

    override fun createDefault(): BoneMassRecord = BoneMassRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        mass = 5.kilograms,
    )
}