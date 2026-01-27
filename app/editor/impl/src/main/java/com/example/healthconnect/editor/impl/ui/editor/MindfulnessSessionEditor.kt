package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.MindfulnessSessionRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.StringComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.MindfulnessSessionModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class MindfulnessSessionEditor() : Editor<MindfulnessSessionRecord, MindfulnessSessionModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: MindfulnessSessionModel,
        event: ModelModificationEvent,
    ): MindfulnessSessionModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnStringValueChanged -> when (event.value.type) {
            is StringComponentModel.Type.MindfulnessSessionTitle -> model.copy(
                title = event.value
            )

            is StringComponentModel.Type.MindfulnessSessionNotes -> model.copy(
                notes = event.value
            )

            else -> throw NotImplementedError()
        }

        is ModelModificationEvent.OnValueSelected -> when (event.selector.type) {
            is SelectorComponentModel.Type.MindfulnessSessionType -> model.copy(
                mindfulnessSessionType = event.selector
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: MindfulnessSessionRecord,
        mapper: MetadataMapper,
    ): MindfulnessSessionModel = MindfulnessSessionModel(
        time = TimeComponentModel.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        title = StringComponentModel(
            value = record.title ?: "",
            type = StringComponentModel.Type.MindfulnessSessionTitle()
        ),
        notes = StringComponentModel(
            value = record.notes ?: "",
            type = StringComponentModel.Type.MindfulnessSessionNotes()
        ),
        mindfulnessSessionType = SelectorComponentModel(
            value = record.mindfulnessSessionType,
            type = SelectorComponentModel.Type.MindfulnessSessionType()
        )
    )

    override fun toRecord(
        validModel: MindfulnessSessionModel,
        mapper: MetadataMapper,
    ): MindfulnessSessionRecord = MindfulnessSessionRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        title = validModel.title.value.ifBlank { null },
        notes = validModel.notes.value.ifBlank { null },
        mindfulnessSessionType = validModel.mindfulnessSessionType.value,
    )

    override fun createDefault(): MindfulnessSessionRecord {
        val instant = Instant.now()
        return MindfulnessSessionRecord(
            startTime = instant.minusSeconds(600),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
            mindfulnessSessionType = MindfulnessSessionRecord.MINDFULNESS_SESSION_TYPE_MEDITATION,
        )
    }
}