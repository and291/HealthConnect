package com.example.healthconnect.ui.navigation

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.healthconnect.di.Di
import com.example.healthconnect.editor.api.navigation.EditorNavigationEntry
import com.example.healthconnect.navigation.api.NavigationEntry
import com.example.healthconnect.ui.navigation.AppNavigationEntry.ProviderUpdateRequired
import com.example.healthconnect.ui.navigation.AppNavigationEntry.Splash
import com.example.healthconnect.ui.navigation.AppNavigationEntry.Unavailable
import com.example.healthconnect.ui.screen.SdkUnavailableScreen
import com.example.healthconnect.ui.screen.SdkUpdateRequiredScreen
import com.example.healthconnect.utilty.api.navigation.UtilityNavigationEntry

// Define keys that will identify content
sealed class AppNavigationEntry : NavigationEntry {
    data object Splash : AppNavigationEntry()
    data object Unavailable : AppNavigationEntry()
    data object ProviderUpdateRequired : AppNavigationEntry()
}


@Composable
fun CreateNavDisplay(
    backStack: SnapshotStateList<NavigationEntry>,
    innerPadding: PaddingValues,
    requestPermission: (String) -> Unit,
    activity: Activity,
    libraryNavigation: LibraryNavigation,
) {
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(), //taken from default parameter
            rememberViewModelStoreNavEntryDecorator(), //separate VM store for each nav entry
        ),
        onBack = { backStack.removeLastOrNull() },
    ) { key ->
        when (key) {
            is AppNavigationEntry -> when (key) {

                is Unavailable -> NavEntry(key) {
                    SdkUnavailableScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                is ProviderUpdateRequired -> NavEntry(key) {
                    SdkUpdateRequiredScreen(
                        startUpdateActivity = {
                            activity.startActivity(libraryNavigation.chooseUpdateLibraryIntent())
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                Splash -> NavEntry(key) { Text("Splash route") }
            }

            is UtilityNavigationEntry -> Di.utilityNav.getNavEntry(
                key = key,
                backStack = backStack,
                requestPermission = requestPermission,
                showInternalDataManager = {
                    activity.startActivity(libraryNavigation.chooseManageDataIntent())
                },
                innerPadding = innerPadding
            )

            is EditorNavigationEntry -> Di.editorNav.getNavEntry(
                key = key,
                backStack = backStack,
                innerPadding = innerPadding
            )

            else -> NavEntry(key) { Text("Unknown route") }
        }
    }
}