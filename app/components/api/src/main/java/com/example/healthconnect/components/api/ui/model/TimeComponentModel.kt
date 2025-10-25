package com.example.healthconnect.components.api.ui.model

import java.time.Instant
import java.time.ZoneOffset

sealed class TimeComponentModel : ComponentModel() {

    data class Valid(
        val instant: Instant,
        val zoneOffset: ZoneOffset?
    ) : TimeComponentModel()

    data object Invalid : TimeComponentModel()
}