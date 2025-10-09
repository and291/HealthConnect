package com.example.healthconnect.ui.screen.component.metadata.mapper

import androidx.health.connect.client.records.metadata.Metadata

class RecordingMethodMapper {

    val recordingMethods = listOf(
        Metadata.Companion.RECORDING_METHOD_UNKNOWN to "UNKNOWN",
        Metadata.Companion.RECORDING_METHOD_ACTIVELY_RECORDED to "ACTIVELY RECORDED",
        Metadata.Companion.RECORDING_METHOD_AUTOMATICALLY_RECORDED to "AUTOMATICALLY RECORDED",
        Metadata.Companion.RECORDING_METHOD_MANUAL_ENTRY to "MANUAL ENTRY"
    )

    fun map(method: Int): String {
        val item = recordingMethods.find { x -> x.first == method }
        return requireNotNull(item?.second) { "Method = $method not found among available recording methods" }
    }

}