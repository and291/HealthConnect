package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.MindfulnessSessionRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.domain.record.MindfulnessSession
import java.time.Instant
import java.time.ZoneOffset

class MindfulnessSessionEditor() : Editor<MindfulnessSessionRecord, MindfulnessSession>() {

    override fun toModel(
        record: MindfulnessSessionRecord,
        mapper: MetadataMapper,
    ): MindfulnessSession = MindfulnessSession(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset,
            priority = 0
        ),
        metadata = mapper.toEntity(record.metadata),
        title = StringField(
            value = record.title ?: "",
            type = StringField.Type.MindfulnessSessionTitle(),
            priority = 2
        ),
        notes = StringField(
            value = record.notes ?: "",
            type = StringField.Type.MindfulnessSessionNotes(),
            priority = 3
        ),
        mindfulnessSessionType = SelectorField(
            value = record.mindfulnessSessionType,
            type = SelectorField.Type.MindfulnessSessionType(),
            priority = 1
        )
    )

    override fun toRecord(
        validModel: MindfulnessSession,
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