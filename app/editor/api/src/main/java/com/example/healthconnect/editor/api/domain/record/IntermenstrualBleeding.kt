package com.example.healthconnect.editor.api.domain.record

import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField

data class IntermenstrualBleeding(
    override val metadata: MetadataField,
    override val time: TimeField,
) : Instantaneous()