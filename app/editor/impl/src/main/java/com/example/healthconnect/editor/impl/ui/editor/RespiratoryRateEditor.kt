package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.ui.model.top.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
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
        time = TimeComponentModel.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        rate = ValueComponentModel.Dbl(
            parsedValue = record.rate,
            type = ValueComponentModel.Type.RespiratoryRate(),
        )
    )

    override fun toRecord(
        validModel: RespiratoryRateModel,
        mapper: MetadataMapper,
    ): RespiratoryRateRecord = RespiratoryRateRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        rate = (validModel.rate as ValueComponentModel.Dbl).parsedValue!!,
    )

    override fun createDefault(): RespiratoryRateRecord = RespiratoryRateRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        rate = 30.0,
    )
}