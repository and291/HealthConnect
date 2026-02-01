package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.components.api.ui.model.TimeModel

sealed class Interval : Model() {

    abstract val time: TimeComponentModel

    fun getStartValidTime(): TimeModel.Valid =
        (time as TimeComponentModel.Interval).start as TimeModel.Valid

    fun getEndValidTime(): TimeModel.Valid =
        (time as TimeComponentModel.Interval).end as TimeModel.Valid
}