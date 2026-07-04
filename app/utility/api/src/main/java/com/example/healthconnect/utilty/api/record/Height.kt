package com.example.healthconnect.utilty.api.record

import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField

data class Height(
    override val metadata: MetadataField,
    override val time: TimeField,
    val height: ValueField,
) : Instantaneous()