package com.example.healthconnect.editor.api.domain.record

import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseLapField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseRouteField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseSegmentField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField

data class ExerciseSession(
    override val metadata: MetadataField,
    override val time: TimeField,
    val title: StringField,
    val notes: StringField,
    val exerciseType: SelectorField,
    val plannedExerciseSessionId: StringField,
    val segments: ListField<ExerciseSegmentField>,
    val laps: ListField<ExerciseLapField>,
    val route: ListField<ExerciseRouteField>,
) : Interval()
