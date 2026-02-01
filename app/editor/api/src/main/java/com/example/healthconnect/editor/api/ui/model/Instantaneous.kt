package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.components.api.ui.model.TimeModel

sealed class Instantaneous : Model() {

    abstract val time: TimeComponentModel

    fun getValidTime(): TimeModel.Valid =
        (time as TimeComponentModel.Instantaneous).time as TimeModel.Valid
}