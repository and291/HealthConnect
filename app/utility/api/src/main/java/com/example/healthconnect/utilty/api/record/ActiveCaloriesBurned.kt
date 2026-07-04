package com.example.healthconnect.utilty.api.record

import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField

data class ActiveCaloriesBurned(
    override val metadata: MetadataField,
    override val time: TimeField,
    val energy: ValueField,
) : Interval()