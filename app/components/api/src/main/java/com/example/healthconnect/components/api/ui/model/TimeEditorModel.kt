package com.example.healthconnect.components.api.ui.model

import java.time.Instant
import java.time.ZoneOffset

sealed class TimeEditorModel {

    data class Valid(
        val instant: Instant,
        val zoneOffset: ZoneOffset?
    ) : TimeEditorModel()

    data object Invalid : TimeEditorModel()
}