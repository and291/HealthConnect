package com.example.healthconnect.ui.navigation

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.health.connect.client.HealthConnectClient

class LibraryNavigation(
    private val applicationContext: Context
) {
    private val providerPackageName: String = "com.google.android.apps.healthdata"

    fun chooseManageDataIntent(): Intent = Intent.createChooser(
        getManageDataIntent(),
        "Choose app to manage data with Health Connect library Data"
    )

    fun chooseUpdateLibraryIntent(): Intent = Intent.createChooser(
        getUpdateIntent(),
        "Choose app to update Health Connect library",
    )

    /**
     * Returns intent to open built-in Manage Data activity
     */
    private fun getManageDataIntent(): Intent {
        return HealthConnectClient.getHealthConnectManageDataIntent(applicationContext)
    }

    private fun getUpdateIntent(): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            setPackage("com.android.vending")
            data = "market://details?id=$providerPackageName&url=healthconnect%3A%2F%2Fonboarding".toUri()
            putExtra("overlay", true)
            putExtra("callerId", applicationContext.packageName)
        }
    }
}