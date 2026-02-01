package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.kilograms
import com.example.healthconnect.components.api.ui.model.top.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
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

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.Mass -> model.copy(
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
        time = TimeComponentModel.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        mass = ValueComponentModel.Dbl(
            parsedValue = record.mass.inKilograms,
            type = ValueComponentModel.Type.Mass(),
        ),
    )

    override fun toRecord(
        validModel: LeanBodyMassModel,
        mapper: MetadataMapper,
    ): LeanBodyMassRecord = LeanBodyMassRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        mass = (validModel.mass as ValueComponentModel.Dbl).parsedValue!!.kilograms,
    )

    override fun createDefault(): LeanBodyMassRecord = LeanBodyMassRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        mass = 60.kilograms,
    )
}