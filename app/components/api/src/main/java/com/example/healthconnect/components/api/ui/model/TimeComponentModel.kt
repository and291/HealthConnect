package com.example.healthconnect.components.api.ui.model

import java.time.Instant
import java.time.ZoneOffset
import java.util.UUID

sealed class TimeComponentModel(override val presentationId: UUID) : ComponentModel(presentationId) {

    data class Instantaneous(
        val time: TimeModel,
        override val presentationId: UUID = UUID.randomUUID(),
    ) : TimeComponentModel(presentationId) {

        constructor(
            instant: Instant,
            zoneOffset: ZoneOffset?,
            id: UUID = UUID.randomUUID(),
        ) : this(
            time = TimeModel.Valid(instant, zoneOffset),
            presentationId = id
        )

        override fun isValid(): Boolean = time is TimeModel.Valid
    }

    data class Interval(
        val start: TimeModel,
        val end: TimeModel,
        override val presentationId: UUID = UUID.randomUUID(),
    ) : TimeComponentModel(presentationId) {

        constructor(
            startTime: Instant,
            startZoneOffset: ZoneOffset?,
            endTime: Instant,
            endZoneOffset: ZoneOffset?,
            id: UUID = UUID.randomUUID(),
        ) : this(
            start = TimeModel.Valid(startTime, startZoneOffset),
            end = TimeModel.Valid(endTime, endZoneOffset),
            presentationId = id
        )

        override fun isValid(): Boolean = start is TimeModel.Valid && end is TimeModel.Valid
    }
}
