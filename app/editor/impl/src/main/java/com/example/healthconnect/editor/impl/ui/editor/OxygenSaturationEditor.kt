package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.percent
import com.example.healthconnect.components.api.ui.model.top.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import com.example.healthconnect.editor.api.ui.model.OxygenSaturationModel
import java.time.Instant
import java.time.ZoneOffset

class OxygenSaturationEditor() : Editor<OxygenSaturationRecord, OxygenSaturationModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: OxygenSaturationModel,
        event: ModelModificationEvent,
    ): OxygenSaturationModel = when (event) {
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
        record: OxygenSaturationRecord,
        mapper: MetadataMapper,
    ): OxygenSaturationModel = OxygenSaturationModel(
        time = TimeComponentModel.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        percentage = ValueComponentModel.Dbl(
            parsedValue = record.percentage.value,
            type = ValueComponentModel.Type.Percentage(),
        )
    )

    override fun toRecord(
        validModel: OxygenSaturationModel,
        mapper: MetadataMapper,
    ): OxygenSaturationRecord = OxygenSaturationRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        percentage = (validModel.percentage as ValueComponentModel.Dbl).parsedValue!!.percent,
    )

    override fun createDefault(): OxygenSaturationRecord = OxygenSaturationRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        percentage = 20.percent,
    )
}