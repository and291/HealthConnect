package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.TimeComponentModel

sealed class Instantaneous : Model() {

    abstract val time: TimeComponentModel
}