package com.example.healthconnect.models.api.domain.record

import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField

data class CervicalMucus(
    override val metadata: MetadataField,
    override val time: TimeField,
    val appearance: SelectorField,
    val sensation: SelectorField,
) : Instantaneous()