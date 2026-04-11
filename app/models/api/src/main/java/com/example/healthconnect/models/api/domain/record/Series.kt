package com.example.healthconnect.models.api.domain.record

import com.example.healthconnect.components.api.domain.entity.Time
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField

sealed class Series : Model() {

    abstract val time: TimeField

    abstract val samples: ListField<*>

    fun getStartValidTime(): Time.Valid =
        (time as TimeField.Interval).start as Time.Valid

    fun getEndValidTime(): Time.Valid =
        (time as TimeField.Interval).end as Time.Valid
}