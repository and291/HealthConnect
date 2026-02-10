package com.example.healthconnect.editor.api.domain.record

import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField

data class FloorsClimbed(
    override val metadata: MetadataField,
    override val time: TimeField,
    val floors: ValueField,
) : Interval()