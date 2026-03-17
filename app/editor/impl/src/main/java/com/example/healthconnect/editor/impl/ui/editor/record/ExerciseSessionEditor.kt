package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.ExerciseLap
import androidx.health.connect.client.records.ExerciseRoute
import androidx.health.connect.client.records.ExerciseRouteResult
import androidx.health.connect.client.records.ExerciseSegment
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.meters
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseLapField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseRouteField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseSegmentField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.domain.record.ExerciseSession
import java.time.Instant
import java.time.ZoneOffset

class ExerciseSessionEditor() : Editor<ExerciseSessionRecord, ExerciseSession>() {

    override fun toModel(
        record: ExerciseSessionRecord,
        mapper: MetadataMapper,
    ): ExerciseSession = ExerciseSession(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset,
            priority = 0,
        ),
        metadata = mapper.toEntity(record.metadata),
        title = StringField(
            value = record.title ?: "",
            type = StringField.Type.ExerciseSessionTitle(),
            priority = 2,
        ),
        notes = StringField(
            value = record.notes ?: "",
            type = StringField.Type.ExerciseSessionNotes(),
            priority = 3,
        ),
        exerciseType = SelectorField(
            value = record.exerciseType,
            type = SelectorField.Type.ExerciseType(),
            priority = 1,
        ),
        plannedExerciseSessionId = StringField(
            value = record.plannedExerciseSessionId ?: "",
            type = StringField.Type.ExerciseSessionPlannedExerciseSessionId()
        ),
        segments = ListField(
            items = record.segments.map {
                ExerciseSegmentField(
                    startTime = it.startTime,
                    endTime = it.endTime,
                    segmentType = it.segmentType,
                    repetitions = it.repetitions
                )
            },
            type = ListField.Type.ExerciseSegments
        ),
        laps = ListField(
            items = record.laps.map {
                ExerciseLapField(
                    startTime = it.startTime,
                    endTime = it.endTime,
                    lengthInMeters = it.length?.inMeters
                )
            },
            type = ListField.Type.ExerciseLaps
        ),
        route = ListField(
            items = (record.exerciseRouteResult as? ExerciseRouteResult.Data)?.exerciseRoute?.route?.map {
                ExerciseRouteField(
                    time = it.time,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    altitude = it.altitude?.inMeters,
                    horizontalAccuracy = it.horizontalAccuracy?.inMeters,
                    verticalAccuracy = it.verticalAccuracy?.inMeters
                )
            } ?: emptyList(),
            type = ListField.Type.ExerciseRoute(
                result = when (record.exerciseRouteResult) {
                    is ExerciseRouteResult.Data -> ListField.Type.ExerciseRoute.RouteResult.Data
                    is ExerciseRouteResult.ConsentRequired -> ListField.Type.ExerciseRoute.RouteResult.ConsentRequired
                    is ExerciseRouteResult.NoData -> ListField.Type.ExerciseRoute.RouteResult.NoData
                    else -> ListField.Type.ExerciseRoute.RouteResult.NoData
                }
            )
        )
    )

    override fun toRecord(
        validModel: ExerciseSession,
        mapper: MetadataMapper,
    ): ExerciseSessionRecord = ExerciseSessionRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        exerciseType = validModel.exerciseType.value,
        title = validModel.title.value.ifBlank { null },
        notes = validModel.notes.value.ifBlank { null },
        segments = validModel.segments.items.map {
            ExerciseSegment(
                startTime = it.startTime,
                endTime = it.endTime,
                segmentType = it.segmentType,
                repetitions = it.repetitions
            )
        },
        laps = validModel.laps.items.map {
            ExerciseLap(
                startTime = it.startTime,
                endTime = it.endTime,
                length = it.lengthInMeters?.meters
            )
        },
        exerciseRoute = validModel.route.items.takeIf { it.isNotEmpty() }?.let {
            ExerciseRoute(
                route = it.map { location ->
                    ExerciseRoute.Location(
                        time = location.time,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        altitude = location.altitude?.meters,
                        horizontalAccuracy = location.horizontalAccuracy?.meters,
                        verticalAccuracy = location.verticalAccuracy?.meters
                    )
                }
            )
        },
        plannedExerciseSessionId = validModel.plannedExerciseSessionId.value.ifBlank { null }
    )

    override fun createDefault(): ExerciseSessionRecord {
        val instant = Instant.now()
        return ExerciseSessionRecord(
            startTime = instant.minusSeconds(3600),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.unknownRecordingMethod(),
            exerciseType = ExerciseSessionRecord.EXERCISE_TYPE_RUNNING,
        )
    }
}