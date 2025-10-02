package com.example.healthconnect.ui.navigation

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.health.connect.client.records.Record
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.healthconnect.ui.navigation.NavDestination.Available
import com.example.healthconnect.ui.navigation.NavDestination.Records
import com.example.healthconnect.ui.screen.InsertRecordScreen
import com.example.healthconnect.ui.screen.RecordsScreen
import com.example.healthconnect.ui.screen.SdkAvailableScreen
import com.example.healthconnect.ui.screen.SdkUnavailableScreen
import com.example.healthconnect.ui.screen.SdkUpdateRequiredScreen
import com.example.healthconnect.ui.screen.record.BasalBodyTemperatureScreen
import com.example.healthconnect.ui.screen.record.metadata.MetadataEditorComponent
import com.example.healthconnect.ui.screen.record.metadata.MetadataEditorViewModel
import kotlin.reflect.KClass

// Define keys that will identify content
sealed class NavDestination {
    data object Splash : NavDestination()
    data object Available : NavDestination()
    data object Unavailable : NavDestination()
    data object ProviderUpdateRequired : NavDestination()
    data class Records(val recordType: KClass<Record>) : NavDestination()
    data class Insert(val recordType: KClass<Record>) : NavDestination()
    data class RecordScreen(val record: Record) : NavDestination()
    data class MetadataScreen(val metadataModel: MetadataEditorViewModel.MetadataModel) :
        NavDestination()
}


@Composable
fun CreateNavDisplay(
    backStack: SnapshotStateList<NavDestination>,
    innerPadding: PaddingValues,
    requestPermission: (String) -> Unit,
    activity: Activity,
) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
    ) { key ->
        when (key) {
            is Available -> NavEntry(key) {
                SdkAvailableScreen(
                    onTypeClick = { type ->
                        backStack.add(Records(type))
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is NavDestination.Unavailable -> NavEntry(key) {
                SdkUnavailableScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is NavDestination.ProviderUpdateRequired -> NavEntry(key) {
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

            is Records -> NavEntry(key) {
                RecordsScreen(
                    requestPermission = requestPermission,
                    onRecordClick = {
                        backStack.add(
                            NavDestination.RecordScreen(
                                record = it
                            )
                        )
                    },
                    onInsertRecordClick = { backStack.add(NavDestination.Insert(key.recordType)) },
                    recordType = key.recordType,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is NavDestination.Insert -> NavEntry(key) {
                InsertRecordScreen(
                    recordType = key.recordType,
                    modifier = Modifier.padding(innerPadding),
                )
            }

            is NavDestination.RecordScreen -> NavEntry(key) {
                BasalBodyTemperatureScreen(
                    record = key.record,
                    onMetadataClick = {
                        backStack.add(NavDestination.MetadataScreen(it))
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is NavDestination.Splash -> NavEntry(key) { Text("Unknown route") }
            is NavDestination.MetadataScreen -> NavEntry(key) {
                MetadataEditorComponent(
                    metadataModel = key.metadataModel,
                    onMetaModelChange = {

                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}