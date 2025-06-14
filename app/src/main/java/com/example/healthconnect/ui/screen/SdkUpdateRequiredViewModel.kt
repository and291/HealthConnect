package com.example.healthconnect.ui.screen

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel

class SdkUpdateRequiredViewModel(
    private val applicationContext: Context
) : ViewModel() {

    private val providerPackageName: String = "com.google.android.apps.healthdata"

    fun getUpdateIntent(): Intent {
        // Optionally redirect to package installer to find a provider, for example:
        val uriString =
            "market://details?id=$providerPackageName&url=healthconnect%3A%2F%2Fonboarding"
        return Intent(Intent.ACTION_VIEW).apply {
            setPackage("com.android.vending")
            data = uriString.toUri()
            putExtra("overlay", true)
            putExtra("callerId", applicationContext.packageName)
        }
    }
}
