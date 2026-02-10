package com.example.healthconnect.components.api.domain.entity

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

sealed class Time {

    abstract val input: String
    abstract val zoneOffset: ZoneOffset?
    abstract val zonedLocalizedTime: String?

    data class Valid(
        val instant: Instant,
        override val zoneOffset: ZoneOffset?
    ) : Time() {

        /**
         * Использую формат ISO_INSTANT, потому что среди всех форматов, которые я успел попробовать,
         * он оказался самым удобным для редактирования через клавиатуру —
         * отображает все компоненты даты и времени в одном месте.
         */
        override val input: String
            get() = instant.toString()

        override val zonedLocalizedTime: String?
            get() {
                val localizedDateTimeFormatter = DateTimeFormatter
                    .ofLocalizedDateTime(FormatStyle.LONG)
                    .withLocale(Locale.getDefault())
                return instant
                    .atZone(zoneOffset ?: ZoneId.systemDefault())
                    .format(localizedDateTimeFormatter)
            }
    }

    data class Invalid(
        override val input: String,
        override val zoneOffset: ZoneOffset?,
    ) : Time() {

        fun getParseErrorMessage(): String = "Failed to parse Instant: $input"

        override val zonedLocalizedTime: String?
            get() = null
    }
}