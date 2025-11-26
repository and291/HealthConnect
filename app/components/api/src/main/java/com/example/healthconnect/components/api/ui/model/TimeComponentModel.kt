package com.example.healthconnect.components.api.ui.model

import java.time.Instant
import java.time.ZoneOffset

sealed class TimeComponentModel : ComponentModel() {

    data class Instantaneous(
        val time: TimeModel,
    ) : TimeComponentModel() {

        constructor(
            instant: Instant,
            zoneOffset: ZoneOffset?,
        ) : this(TimeModel.Valid(instant, zoneOffset))

        override fun isValid(): Boolean = time is TimeModel.Valid
    }

    data class Interval(
        val start: TimeModel,
        val end: TimeModel,
    ) : TimeComponentModel() {

        constructor(
            startTime: Instant,
            startZoneOffset: ZoneOffset?,
            endTime: Instant,
            endZoneOffset: ZoneOffset?,
        ) : this(
            start = TimeModel.Valid(startTime, startZoneOffset),
            end = TimeModel.Valid(endTime, endZoneOffset)
        )

        override fun isValid(): Boolean = start is TimeModel.Valid && end is TimeModel.Valid
    }
}