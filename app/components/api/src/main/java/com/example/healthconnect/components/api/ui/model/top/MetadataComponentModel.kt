package com.example.healthconnect.components.api.ui.model.top

import android.util.Log
import com.example.healthconnect.components.api.ui.model.sub.DeviceComponentModel
import java.time.Instant
import java.util.UUID

data class MetadataComponentModel(
    val recordingMethod: Int,
    val id: String = "",
    val dataOriginPackageName: String = "",
    val lastModifiedTime: Instant = Instant.EPOCH,
    val clientRecordId: String = "",
    val clientRecordVersion: String = "",
    val deviceComponentModel: DeviceComponentModel = DeviceComponentModel.Empty,
    override val presentationId: UUID = UUID.randomUUID(),
) : TopLevelComponentModel(presentationId) {

    override fun isValid(): Boolean {
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