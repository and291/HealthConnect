package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.MenstruationFlowRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.ui.model.top.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.MenstruationFlowModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class MenstruationFlowEditor() : Editor<MenstruationFlowRecord, MenstruationFlowModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: MenstruationFlowModel,
        event: ModelModificationEvent,
    ): MenstruationFlowModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueSelected -> when (event.selector.type) {

            is SelectorComponentModel.Type.Flow -> model.copy(
                flow = SelectorComponentModel(
                    value = event.selector.value,
                    type = event.selector.type,
                )
            )
            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: MenstruationFlowRecord,
        mapper: MetadataMapper,
    ): MenstruationFlowModel = MenstruationFlowModel(
        time = TimeComponentModel.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        flow = SelectorComponentModel(
            value = record.flow,
            type = SelectorComponentModel.Type.Flow(),
        ),
    )

    override fun toRecord(
        validModel: MenstruationFlowModel,
        mapper: MetadataMapper,
    ): MenstruationFlowRecord = MenstruationFlowRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        flow = validModel.flow.value,
    )

    override fun createDefault(): MenstruationFlowRecord = MenstruationFlowRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
    )
}