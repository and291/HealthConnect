package com.example.healthconnect.editor.api.domain.record

import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.components.api.domain.entity.field.composite.PlannedExerciseBlockField

data class PlannedExerciseSession( //TODO support second constructor
    override val metadata: MetadataField,
    override val time: TimeField,
    val title: StringField,
    val notes: StringField,
    val exerciseType: SelectorField,
    val blocks: ListField<PlannedExerciseBlockField>,
) : Interval()
