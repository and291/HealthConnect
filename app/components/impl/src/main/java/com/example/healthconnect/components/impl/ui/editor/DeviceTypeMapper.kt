package com.example.healthconnect.components.impl.ui.editor

import androidx.health.connect.client.records.metadata.Device

class DeviceTypeMapper {

    val deviceTypes = listOf(
        Device.TYPE_UNKNOWN to "UNKNOWN",
        Device.TYPE_WATCH to "WATCH",
        Device.TYPE_PHONE to "PHONE",
        Device.TYPE_SCALE to "SCALE",
        Device.TYPE_RING to "RING",
        Device.TYPE_HEAD_MOUNTED to "HEAD MOUNTED",
        Device.TYPE_FITNESS_BAND to "FITNESS BAND",
        Device.TYPE_CHEST_STRAP to "CHEST STRAP",
        Device.TYPE_SMART_DISPLAY to "SMART DISPLAY",
        Device.TYPE_CONSUMER_MEDICAL_DEVICE to "CONSUMER MEDICAL DEVICE",
        Device.TYPE_GLASSES to "GLASSES",
        Device.TYPE_HEARABLE to "HEARABLE",
        Device.TYPE_FITNESS_MACHINE to "FITNESS MACHINE",
        Device.TYPE_FITNESS_EQUIPMENT to "FITNESS EQUIPMENT",
        Device.TYPE_PORTABLE_COMPUTER to "PORTABLE COMPUTER",
        Device.TYPE_METER to "METER",
    )

    fun mapName(type: Int): String {
        val item = deviceTypes.find { x -> x.first == type }
        return requireNotNull(item?.second) {
            "Type = $type not found among available device types"
        }
    }
}