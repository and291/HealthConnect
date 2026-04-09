package com.example.healthconnect.models.api.domain.record

import com.example.healthconnect.components.api.domain.entity.field.atomic.SleepSessionStageField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField

data class SleepSession(
    override val metadata: MetadataField,
    override val time: TimeField,
    val title: StringField,
    val notes: StringField,
    val stages: ListField<SleepSessionStageField>,
) : Interval()
