package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.SleepSessionStageField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.models.api.domain.record.SleepSession
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class SleepSessionEditor() : Editor<SleepSessionRecord, SleepSession>() {

    override fun toModel(
        record: SleepSessionRecord,
        mapper: MetadataMapper,
    ): SleepSession = SleepSession(
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
            priority = 1
        ),
        notes = StringField(
            value = record.notes ?: "",
            type = StringField.Type.MindfulnessSessionNotes(),
            priority = 2
        ),
        stages = ListField(
            items = record.stages.map {
                SleepSessionStageField(
                    startTime = StringField(
                        value = it.startTime.toString(),
                        type = StringField.Type.SleepSessionStageTime("Start Time")
                    ),
                    endTime = StringField(
                        value = it.endTime.toString(),
                        type = StringField.Type.SleepSessionStageTime("End Time")
                    ),
                    stage = it.stage
                )
            },
            type = ListField.Type.SleepSessionStages,
            priority = 3
        )
    )

    override fun toRecord(
        validModel: SleepSession,
        mapper: MetadataMapper,
    ): SleepSessionRecord = SleepSessionRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        title = validModel.title.value.ifBlank { null },
        notes = validModel.notes.value.ifBlank { null },
        stages = validModel.stages.items.map {
            SleepSessionRecord.Stage(
                startTime = Instant.parse(it.startTime.value),
                endTime = Instant.parse(it.endTime.value),
                stage = it.stage
            )
        }
    )

    override fun createDefault(): SleepSessionRecord {
        val instant = Instant.now()
        return SleepSessionRecord(
            startTime = instant.minusSeconds(28800), // 8 hours
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.unknownRecordingMethod(),
        )
    }
}
