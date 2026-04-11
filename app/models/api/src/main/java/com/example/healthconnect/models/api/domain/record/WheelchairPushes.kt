package com.example.healthconnect.models.api.domain.record

import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField

data class WheelchairPushes(
    override val metadata: MetadataField,
    override val time: TimeField,
    val count: ValueField,
) : Interval()
