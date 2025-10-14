package com.example.healthconnect.ui.navigation

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.navigation.AppNavigationEntry.Available
import com.example.healthconnect.ui.navigation.AppNavigationEntry.ProviderUpdateRequired
import com.example.healthconnect.ui.navigation.AppNavigationEntry.Splash
import com.example.healthconnect.ui.navigation.AppNavigationEntry.Unavailable
import com.example.healthconnect.ui.screen.SdkAvailableScreen
import com.example.healthconnect.ui.screen.SdkUnavailableScreen
import com.example.healthconnect.ui.screen.SdkUpdateRequiredScreen
import com.example.healthconnect.utilty.api.navigation.NavigationEntry
import com.example.healthconnect.utilty.api.navigation.UtilityNavigationEntry

// Define keys that will identify content
sealed class AppNavigationEntry : NavigationEntry {
    data object Splash : AppNavigationEntry()
    data object Available : AppNavigationEntry()
    data object Unavailable : AppNavigationEntry()
    data object ProviderUpdateRequired : AppNavigationEntry()
}


@Composable
fun CreateNavDisplay(
    backStack: SnapshotStateList<NavigationEntry>,
    innerPadding: PaddingValues,
    requestPermission: (String) -> Unit,
    activity: Activity,
) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
    ) { key ->
        when (key) {
            is AppNavigationEntry -> when (key) {
                is Available -> NavEntry(key) {
                    SdkAvailableScreen(
                        onTypeClick = { type ->
                            backStack.add(UtilityNavigationEntry.Records(type))
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                is Unavailable -> NavEntry(key) {
                    SdkUnavailableScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                is ProviderUpdateRequired -> NavEntry(key) {
                    SdkUpdateRequiredScreen(
                        startUpdateActivity = { intent ->
                            activity.startActivity(
                                Intent.createChooser(
                                    intent,
                                    "Choose app to update Health Connect library"
                                )
                            )
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                Splash -> NavEntry(key) { Text("Splash route") }
            }

            is UtilityNavigationEntry -> Di.utilityNavigationEntryProvider.getNavEntry(
                key = key,
                backStack = backStack,
                requestPermission = requestPermission,
                innerPadding = innerPadding
            )

            else -> NavEntry(key) { Text("Unknown route") }
        }
    }
}