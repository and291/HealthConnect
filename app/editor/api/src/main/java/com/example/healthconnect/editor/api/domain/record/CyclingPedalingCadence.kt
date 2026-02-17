package com.example.healthconnect.editor.api.domain.record

import com.example.healthconnect.components.api.domain.entity.field.atomic.CyclingPedalingCadenceSampleField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField

data class CyclingPedalingCadence(
    override val time: TimeField,
    override val metadata: MetadataField,
    override val samples: ListField<CyclingPedalingCadenceSampleField>,
) : Series()
