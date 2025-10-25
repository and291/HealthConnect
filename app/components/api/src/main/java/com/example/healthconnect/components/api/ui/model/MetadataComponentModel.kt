package com.example.healthconnect.components.api.ui.model

import android.util.Log
import java.time.Instant

data class MetadataComponentModel(
    val recordingMethod: Int,
    val id: String = "",
    val dataOriginPackageName: String = "",
    val lastModifiedTime: Instant = Instant.EPOCH,
    val clientRecordId: String = "",
    val clientRecordVersion: String = "",
    val deviceComponentModel: DeviceComponentModel = DeviceComponentModel.Empty
) : ComponentModel() {

    fun isValid(): Boolean {
        //TODO add recording method validity check?
        return try {
            clientRecordVersion.toLong()
            true
        } catch (e: Exception) {
            Log.d(this::class.simpleName, "Validation", e)
            false
        }
    }
}