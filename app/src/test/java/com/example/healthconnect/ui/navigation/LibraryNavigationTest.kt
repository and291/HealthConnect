package com.example.healthconnect.ui.navigation

import android.app.Application
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class LibraryNavigationTest {

    private val context: Application = ApplicationProvider.getApplicationContext()
    private val navigation = LibraryNavigation(context)

    // region chooseUpdateLibraryIntent

    @Test
    fun chooseUpdateLibraryIntent_returnsChooserIntent() {
        val result = navigation.chooseUpdateLibraryIntent()
        assertEquals(Intent.ACTION_CHOOSER, result.action)
    }

    @Test
    fun chooseUpdateLibraryIntent_hasExpectedTitle() {
        val result = navigation.chooseUpdateLibraryIntent()
        assertEquals(
            "Choose app to update Health Connect library",
            result.getStringExtra(Intent.EXTRA_TITLE)
        )
    }

    @Test
    fun chooseUpdateLibraryIntent_innerIntent_hasViewAction() {
        val inner = innerIntentOf(navigation.chooseUpdateLibraryIntent())
        assertEquals(Intent.ACTION_VIEW, inner?.action)
    }

    @Test
    fun chooseUpdateLibraryIntent_innerIntent_targetsPlayStore() {
        val inner = innerIntentOf(navigation.chooseUpdateLibraryIntent())
        assertEquals("com.android.vending", inner?.`package`)
    }

    @Test
    fun chooseUpdateLibraryIntent_innerIntent_hasMarketUri() {
        val inner = innerIntentOf(navigation.chooseUpdateLibraryIntent())
        assertEquals(
            "market://details?id=com.google.android.apps.healthdata&url=healthconnect%3A%2F%2Fonboarding",
            inner?.dataString
        )
    }

    @Test
    fun chooseUpdateLibraryIntent_innerIntent_hasOverlayExtra() {
        val inner = innerIntentOf(navigation.chooseUpdateLibraryIntent())
        assertEquals(true, inner?.getBooleanExtra("overlay", false))
    }

    @Test
    fun chooseUpdateLibraryIntent_innerIntent_hasCallerIdMatchingPackageName() {
        val inner = innerIntentOf(navigation.chooseUpdateLibraryIntent())
        assertEquals(context.packageName, inner?.getStringExtra("callerId"))
    }

    // endregion

    // region chooseManageDataIntent

    @Test
    fun chooseManageDataIntent_returnsChooserIntent() {
        val result = navigation.chooseManageDataIntent()
        assertEquals(Intent.ACTION_CHOOSER, result.action)
    }

    @Test
    fun chooseManageDataIntent_hasExpectedTitle() {
        val result = navigation.chooseManageDataIntent()
        assertEquals(
            "Choose app to manage data with Health Connect library Data",
            result.getStringExtra(Intent.EXTRA_TITLE)
        )
    }

    @Test
    fun chooseManageDataIntent_wrapsNonNullInnerIntent() {
        val result = navigation.chooseManageDataIntent()
        assertNotNull(innerIntentOf(result))
    }

    // endregion

    @Suppress("DEPRECATION")
    private fun innerIntentOf(chooser: Intent): Intent? =
        chooser.getParcelableExtra(Intent.EXTRA_INTENT)
}
