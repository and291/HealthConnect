package com.example.healthconnect.editor.api.domain.record

import com.example.healthconnect.components.api.domain.entity.Time
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField

sealed class Instantaneous : Model() {

    abstract val time: TimeField

    fun getValidTime(): Time.Valid =
        (time as TimeField.Instantaneous).time as Time.Valid
}