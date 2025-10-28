package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.OvulationTestRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.OvulationTestModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class OvulationTestEditor() : Editor<OvulationTestRecord, OvulationTestModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: OvulationTestModel,
        event: ModelModificationEvent,
    ): OvulationTestModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueSelected -> when (event.selector.type) {

            is SelectorComponentModel.Type.Result -> model.copy(
                result = SelectorComponentModel.Valid(
                    value = event.selector.value,
                    type = event.selector.type,
                )
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: OvulationTestRecord,
        mapper: MetadataMapper,
    ): OvulationTestModel = OvulationTestModel(
        time = TimeComponentModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        result = SelectorComponentModel.Valid(
            value = record.result,
            type = SelectorComponentModel.Type.Result(),
        ),
    )

    override fun toRecord(
        validModel: OvulationTestModel,
        mapper: MetadataMapper,
    ): OvulationTestRecord = OvulationTestRecord(
        time = (validModel.time as TimeComponentModel.Valid).instant,
        zoneOffset = (validModel.time as TimeComponentModel.Valid).zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        result = validModel.result.value,
    )

    override fun createDefault(): OvulationTestRecord = OvulationTestRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        result = OvulationTestRecord.RESULT_INCONCLUSIVE,
    )
}