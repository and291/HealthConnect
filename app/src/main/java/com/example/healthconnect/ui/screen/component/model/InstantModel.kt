package com.example.healthconnect.ui.screen.component.model

import java.time.Instant
import java.time.ZoneOffset

//TODO to API
sealed class InstantModel {

    data class Valid(
        val instant: Instant,
        val zoneOffset: ZoneOffset?
    ) : InstantModel()

    data object Invalid : InstantModel()
}