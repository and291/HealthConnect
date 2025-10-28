package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.kilograms
import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.LeanBodyMassModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class LeanBodyMassEditor() : Editor<LeanBodyMassRecord, LeanBodyMassModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: LeanBodyMassModel,
        event: ModelModificationEvent,
    ): LeanBodyMassModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueComponentModel.Type.Mass -> model.copy(
                mass = event.value
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: LeanBodyMassRecord,
        mapper: MetadataMapper,
    ): LeanBodyMassModel = LeanBodyMassModel(
        time = TimeComponentModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        mass = DoubleValueComponentModel.Valid(
            parsedValue = record.mass.inKilograms,
            type = DoubleValueComponentModel.Type.Mass(),
        ),
    )

    override fun toRecord(
        validModel: LeanBodyMassModel,
        mapper: MetadataMapper,
    ): LeanBodyMassRecord = LeanBodyMassRecord(
        time = (validModel.time as TimeComponentModel.Valid).instant,
        zoneOffset = (validModel.time as TimeComponentModel.Valid).zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        mass = (validModel.mass as DoubleValueComponentModel.Valid).parsedValue.kilograms,
    )

    override fun createDefault(): LeanBodyMassRecord = LeanBodyMassRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        mass = 60.kilograms,
    )
}