package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.ExerciseLap
import androidx.health.connect.client.records.ExerciseRoute
import androidx.health.connect.client.records.ExerciseRouteResult
import androidx.health.connect.client.records.ExerciseSegment
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.meters
import com.example.healthconnect.components.api.ui.model.sub.ExerciseLapComponentModel
import com.example.healthconnect.components.api.ui.model.sub.ExerciseRouteComponentModel
import com.example.healthconnect.components.api.ui.model.sub.ExerciseSegmentComponentModel
import com.example.healthconnect.components.api.ui.model.top.ListComponentModel
import com.example.healthconnect.components.api.ui.model.top.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.top.StringComponentModel
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.ExerciseSessionModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class ExerciseSessionEditor() : Editor<ExerciseSessionRecord, ExerciseSessionModel>() {

    @Suppress("UNCHECKED_CAST", "REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: ExerciseSessionModel,
        event: ModelModificationEvent,
    ): ExerciseSessionModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnStringValueChanged -> when (event.value.type) {
            is StringComponentModel.Type.ExerciseSessionTitle -> model.copy(
                title = event.value
            )

            is StringComponentModel.Type.ExerciseSessionNotes -> model.copy(
                notes = event.value
            )

            is StringComponentModel.Type.ExerciseSessionPlannedExerciseSessionId -> model.copy(
                plannedExerciseSessionId = event.value
            )

            else -> throw NotImplementedError()
        }

        is ModelModificationEvent.OnValueSelected -> when (event.selector.type) {
            is SelectorComponentModel.Type.ExerciseType -> model.copy(
                exerciseType = event.selector
            )

            else -> throw NotImplementedError()
        }

        is ModelModificationEvent.OnListChanged -> when (event.list.type) {
            is ListComponentModel.Type.ExerciseSegments -> model.copy(
                segments = event.list as ListComponentModel<ExerciseSegmentComponentModel>
            )

            is ListComponentModel.Type.ExerciseLaps -> model.copy(
                laps = event.list as ListComponentModel<ExerciseLapComponentModel>
            )

            is ListComponentModel.Type.ExerciseRoute -> model.copy(
                route = event.list as ListComponentModel<ExerciseRouteComponentModel>
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: ExerciseSessionRecord,
        mapper: MetadataMapper,
    ): ExerciseSessionModel = ExerciseSessionModel(
        time = TimeComponentModel.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        title = StringComponentModel(
            value = record.title ?: "",
            type = StringComponentModel.Type.ExerciseSessionTitle()
        ),
        notes = StringComponentModel(
            value = record.notes ?: "",
            type = StringComponentModel.Type.ExerciseSessionNotes()
        ),
        exerciseType = SelectorComponentModel(
            value = record.exerciseType,
            type = SelectorComponentModel.Type.ExerciseType()
        ),
        plannedExerciseSessionId = StringComponentModel(
            value = record.plannedExerciseSessionId ?: "",
            type = StringComponentModel.Type.ExerciseSessionPlannedExerciseSessionId()
        ),
        segments = ListComponentModel(
            items = record.segments.map {
                ExerciseSegmentComponentModel(
                    startTime = it.startTime,
                    endTime = it.endTime,
                    segmentType = it.segmentType,
                    repetitions = it.repetitions
                )
            },
            type = ListComponentModel.Type.ExerciseSegments
        ),
        laps = ListComponentModel(
            items = record.laps.map {
                ExerciseLapComponentModel(
                    startTime = it.startTime,
                    endTime = it.endTime,
                    lengthInMeters = it.length?.inMeters
                )
            },
            type = ListComponentModel.Type.ExerciseLaps
        ),
        route = ListComponentModel(
            items = (record.exerciseRouteResult as? ExerciseRouteResult.Data)?.exerciseRoute?.route?.map {
                ExerciseRouteComponentModel(
                    time = it.time,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    altitude = it.altitude?.inMeters,
                    horizontalAccuracy = it.horizontalAccuracy?.inMeters,
                    verticalAccuracy = it.verticalAccuracy?.inMeters
                )
            } ?: emptyList(),
            type = ListComponentModel.Type.ExerciseRoute(
                result = when (record.exerciseRouteResult) {
                    is ExerciseRouteResult.Data -> ListComponentModel.Type.ExerciseRoute.RouteResult.Data
                    is ExerciseRouteResult.ConsentRequired -> ListComponentModel.Type.ExerciseRoute.RouteResult.ConsentRequired
                    is ExerciseRouteResult.NoData -> ListComponentModel.Type.ExerciseRoute.RouteResult.NoData
                    else -> ListComponentModel.Type.ExerciseRoute.RouteResult.NoData
                }
            )
        )
    )

    override fun toRecord(
        validModel: ExerciseSessionModel,
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
        segments = (validModel.segments.items as List<ExerciseSegmentComponentModel>).map {
            ExerciseSegment(
                startTime = it.startTime,
                endTime = it.endTime,
                segmentType = it.segmentType,
                repetitions = it.repetitions
            )
        },
        laps = (validModel.laps.items as List<ExerciseLapComponentModel>).map {
            ExerciseLap(
                startTime = it.startTime,
                endTime = it.endTime,
                length = it.lengthInMeters?.meters
            )
        },
        exerciseRoute = (validModel.route.items as List<ExerciseRouteComponentModel>).takeIf { it.isNotEmpty() }?.let {
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
