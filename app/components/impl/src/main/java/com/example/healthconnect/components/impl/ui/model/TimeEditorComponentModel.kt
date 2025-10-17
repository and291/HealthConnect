package com.example.healthconnect.components.impl.ui.model

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

data class TimeEditorComponentModel(
    val timeModel: TimeModel,
    val zoneId: ZoneId? = null,
    val setZoneAttemptErrorMessage: String? = null,
) {
    fun getTimeInputValue(): String = when (timeModel) {
        is TimeModel.Invalid -> timeModel.input
        // Использую формат ISO_INSTANT, потому что среди всех форматов, которые я успел попробовать,
        // он оказался самым удобным для редактирования через клавиатуру —
        // отображает все компоненты даты и времени в одном месте.
        is TimeModel.Valid -> timeModel.instant.toString()
    }

    fun getZonedLocalizedTime(): String? = when (timeModel) {
        is TimeModel.Invalid -> null
        is TimeModel.Valid -> {
            val localizedDateTimeFormatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.LONG)
                .withLocale(Locale.getDefault())
            timeModel.instant
                .atZone(zoneId ?: ZoneOffset.systemDefault())
                .format(localizedDateTimeFormatter)
        }
    }

    sealed class TimeModel {

        data class Valid(
            val instant: Instant
        ) : TimeModel()

        data class Invalid(
            val input: String,
            val parseErrorMessage: String
        ) : TimeModel()
    }

    companion object {

        fun create(
            instant: Instant,
            zoneOffset: ZoneOffset?
        ): TimeEditorComponentModel = TimeEditorComponentModel(
            timeModel = TimeModel.Valid(instant),
            zoneId = zoneOffset,
            setZoneAttemptErrorMessage = null,
        )
    }
}