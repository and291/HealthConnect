package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.TimeComponentModel

sealed class Interval : Model() {

    abstract val time: TimeComponentModel.Interval
}