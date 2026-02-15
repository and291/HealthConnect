package com.example.healthconnect.editor.api.domain.record

import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SkinTemperatureDeltaField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField

data class SkinTemperature(
    override val metadata: MetadataField,
    override val time: TimeField,
    val baseline: ValueField,
    val measurementLocation: SelectorField,
    val deltas: ListField<SkinTemperatureDeltaField>,
) : Interval()
