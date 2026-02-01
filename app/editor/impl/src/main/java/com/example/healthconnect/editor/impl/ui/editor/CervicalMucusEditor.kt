package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.CervicalMucusRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.ui.model.top.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.CervicalMucusModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class CervicalMucusEditor() : Editor<CervicalMucusRecord, CervicalMucusModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: CervicalMucusModel,
        event: ModelModificationEvent,
    ): CervicalMucusModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueSelected -> when (event.selector.type) {

            is SelectorComponentModel.Type.Appearance -> model.copy(
                appearance = SelectorComponentModel(
                    value = event.selector.value,
                    type = event.selector.type,
                )
            )
            is SelectorComponentModel.Type.Sensation -> model.copy(
                sensation = SelectorComponentModel(
                    value = event.selector.value,
                    type = event.selector.type,
                )
            )
            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: CervicalMucusRecord,
        mapper: MetadataMapper,
    ): CervicalMucusModel = CervicalMucusModel(
        time = TimeComponentModel.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        appearance = SelectorComponentModel(
            value = record.appearance,
            type = SelectorComponentModel.Type.Appearance(),
        ),
        sensation = SelectorComponentModel(
            value = record.sensation,
            type = SelectorComponentModel.Type.Sensation(),
        ),
    )

    override fun toRecord(
        validModel: CervicalMucusModel,
        mapper: MetadataMapper,
    ): CervicalMucusRecord = CervicalMucusRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        appearance = validModel.appearance.value,
        sensation = validModel.sensation.value
    )

    override fun createDefault(): CervicalMucusRecord = CervicalMucusRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
    )
}