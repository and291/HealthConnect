package com.example.healthconnect.ui.screen.component.metadata.mapper

import androidx.health.connect.client.records.metadata.Device

class DeviceTypeMapper {

    val deviceTypes = listOf(
        Device.Companion.TYPE_UNKNOWN to "UNKNOWN",
        Device.Companion.TYPE_WATCH to "WATCH",
        Device.Companion.TYPE_PHONE to "PHONE",
        Device.Companion.TYPE_SCALE to "SCALE",
        Device.Companion.TYPE_RING to "RING",
        Device.Companion.TYPE_HEAD_MOUNTED to "HEAD MOUNTED",
        Device.Companion.TYPE_FITNESS_BAND to "FITNESS BAND",
        Device.Companion.TYPE_CHEST_STRAP to "CHEST STRAP",
        Device.Companion.TYPE_SMART_DISPLAY to "SMART DISPLAY",
    )

    fun mapName(type: Int): String {
        val item = deviceTypes.find { x -> x.first == type }
        return requireNotNull(item?.second) {
            "Type = $type not found among available device types"
        }
    }
}