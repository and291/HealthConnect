package com.example.healthconnect.components.api.domain.entity.metadata

import android.util.Log
import com.example.healthconnect.components.api.ui.model.DeviceModel
import java.time.Instant

data class MetadataEntity(
    val recordingMethod: Int,
    val id: String = "",
    val dataOriginPackageName: String = "",
    val lastModifiedTime: Instant = Instant.EPOCH,
    val clientRecordId: String = "",
    val clientRecordVersion: String = "",
    val deviceModel: DeviceModel = DeviceModel.Empty
) {

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

