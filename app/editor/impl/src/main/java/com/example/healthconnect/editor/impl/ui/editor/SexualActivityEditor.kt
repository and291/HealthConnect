package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.SexualActivityRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.SexualActivityModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class SexualActivityEditor() : Editor<SexualActivityRecord, SexualActivityModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: SexualActivityModel,
        event: ModelModificationEvent,
    ): SexualActivityModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueSelected -> when (event.selector.type) {

            is SelectorComponentModel.Type.ProtectionUsed -> model.copy(
                protectionUsed = SelectorComponentModel.Valid(
                    value = event.selector.value,
                    type = event.selector.type,
                )
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: SexualActivityRecord,
        mapper: MetadataMapper,
    ): SexualActivityModel = SexualActivityModel(
        time = TimeComponentModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        protectionUsed = SelectorComponentModel.Valid(
            value = record.protectionUsed,
            type = SelectorComponentModel.Type.ProtectionUsed(),
        ),
    )

    override fun toRecord(
        validModel: SexualActivityModel,
        mapper: MetadataMapper,
    ): SexualActivityRecord = SexualActivityRecord(
        time = (validModel.time as TimeComponentModel.Valid).instant,
        zoneOffset = (validModel.time as TimeComponentModel.Valid).zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        protectionUsed = validModel.protectionUsed.value,
    )

    override fun createDefault(): SexualActivityRecord = SexualActivityRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
    )
}