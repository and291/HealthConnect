package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import com.example.healthconnect.editor.api.ui.model.RespiratoryRateModel
import java.time.Instant
import java.time.ZoneOffset

class RespiratoryRateEditor() : Editor<RespiratoryRateRecord, RespiratoryRateModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: RespiratoryRateModel,
        event: ModelModificationEvent,
    ): RespiratoryRateModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.RespiratoryRate -> model.copy(
                rate = event.value
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: RespiratoryRateRecord,
        mapper: MetadataMapper,
    ): RespiratoryRateModel = RespiratoryRateModel(
        time = TimeComponentModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        rate = ValueComponentModel.ValidDouble(
            parsedValue = record.rate,
            type = ValueComponentModel.Type.RespiratoryRate(),
        )
    )

    override fun toRecord(
        validModel: RespiratoryRateModel,
        mapper: MetadataMapper,
    ): RespiratoryRateRecord = RespiratoryRateRecord(
        time = (validModel.time as TimeComponentModel.Valid).instant,
        zoneOffset = (validModel.time as TimeComponentModel.Valid).zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        rate = (validModel.rate as ValueComponentModel.ValidDouble).parsedValue,
    )

    override fun createDefault(): RespiratoryRateRecord = RespiratoryRateRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        rate = 30.0,
    )
}