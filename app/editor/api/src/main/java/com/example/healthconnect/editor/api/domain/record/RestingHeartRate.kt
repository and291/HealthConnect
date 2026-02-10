package com.example.healthconnect.editor.api.domain.record

import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField

data class RestingHeartRate(
    override val metadata: MetadataField,
    override val time: TimeField,
    //Heart beats per minute. Required field. Validation range: 1-300.
    val beatsPerMinute: ValueField,
) : Instantaneous()