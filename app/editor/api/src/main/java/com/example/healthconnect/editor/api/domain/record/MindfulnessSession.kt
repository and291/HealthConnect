package com.example.healthconnect.editor.api.domain.record

import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField

data class MindfulnessSession(
    override val metadata: MetadataField,
    override val time: TimeField,
    val title: StringField,
    val notes: StringField,
    val mindfulnessSessionType: SelectorField,
) : Interval()