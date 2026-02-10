package com.example.healthconnect.editor.api.domain.record

import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField

data class RespiratoryRate(
    override val metadata: MetadataField,
    override val time: TimeField,
    val rate: ValueField,
) : Instantaneous()