package com.example.healthconnect.components.api.domain.entity.field.atomic

import com.example.healthconnect.components.api.domain.entity.Time
import java.time.Instant
import java.time.ZoneOffset
import java.util.UUID

sealed class TimeField(override val instanceId: UUID) : Atomic(instanceId) {

    data class Instantaneous(
        val time: Time,
        override val instanceId: UUID = UUID.randomUUID(),
    ) : TimeField(instanceId) {

        constructor(
            instant: Instant,
            zoneOffset: ZoneOffset?,
            presentationId: UUID = UUID.randomUUID(),
        ) : this(
            time = Time.Valid(instant, zoneOffset),
            instanceId = presentationId
        )

        override fun isValid(): Boolean = time is Time.Valid
    }

    data class Interval(
        val start: Time,
        val end: Time,
        override val instanceId: UUID = UUID.randomUUID(),
    ) : TimeField(instanceId) {

        constructor(
            startTime: Instant,
            startZoneOffset: ZoneOffset?,
            endTime: Instant,
            endZoneOffset: ZoneOffset?,
            presentationId: UUID = UUID.randomUUID(),
        ) : this(
            start = Time.Valid(startTime, startZoneOffset),
            end = Time.Valid(endTime, endZoneOffset),
            instanceId = presentationId
        )

        override fun isValid(): Boolean = start is Time.Valid && end is Time.Valid
    }
}