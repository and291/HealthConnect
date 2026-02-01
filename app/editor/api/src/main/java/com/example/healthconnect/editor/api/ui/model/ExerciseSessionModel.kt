package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.top.ListComponentModel
import com.example.healthconnect.components.api.ui.model.top.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.top.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.top.StringComponentModel
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel

data class ExerciseSessionModel(
    override val metadata: MetadataComponentModel,
    override val time: TimeComponentModel,
    val title: StringComponentModel,
    val notes: StringComponentModel,
    val exerciseType: SelectorComponentModel,
    val plannedExerciseSessionId: StringComponentModel,
    val segments: ListComponentModel<*>,
    val laps: ListComponentModel<*>,
    val route: ListComponentModel<*>,
) : Interval()
