package com.example.healthconnect.components.api.ui.model

import java.time.Instant
import java.time.ZoneOffset

sealed class InstantModel {

    data class Valid(
        val instant: Instant,
        val zoneOffset: ZoneOffset?
    ) : InstantModel()

    data object Invalid : InstantModel()
}