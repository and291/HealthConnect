package com.example.healthconnect

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_AVAILABLE
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_UNAVAILABLE
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.SexualActivityRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.healthconnect.ui.theme.HealthConnectTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

class MainActivity : ComponentActivity() {

    sealed class State {

        data object Unavailable : State()

        data class UpdateRequired(
            private val providerPackageName: String = "com.google.android.apps.healthdata"
        ) : State() {

            fun getUpdateIntent(packageName: String): Intent {
                // Optionally redirect to package installer to find a provider, for example:
                val uriString =
                    "market://details?id=$providerPackageName&url=healthconnect%3A%2F%2Fonboarding"
                return Intent(Intent.ACTION_VIEW).apply {
                    setPackage("com.android.vending")
                    data = Uri.parse(uriString)
                    putExtra("overlay", true)
                    putExtra("callerId", packageName)
                }
            }
        }

        data class Available(
            private val activity: ComponentActivity,
            private val healthConnectClient: HealthConnectClient,
            private val permissions: Set<String> = setOf(
                HealthPermission.getReadPermission(StepsRecord::class),
                HealthPermission.getWritePermission(StepsRecord::class),
                HealthPermission.getReadPermission(SexualActivityRecord::class),
                HealthPermission.getWritePermission(SexualActivityRecord::class),
            )
        ) : State() {

            private val requestPermissionActivityContract =
                PermissionController.createRequestPermissionResultContract()

            val requestPermissions =
                activity.registerForActivityResult(requestPermissionActivityContract) { granted ->
                    if (granted.containsAll(permissions)) {
                        Toast.makeText(
                            activity,
                            "Permissions successfully granted",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(activity, "Lack of required permissions", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            suspend fun checkPermissionsAndRun(
                action: suspend () -> Unit
            ) {
                val granted = healthConnectClient.permissionController.getGrantedPermissions()
                if (granted.containsAll(permissions)) {
                    // Permissions already granted; proceed with inserting or reading data
                    action()
                } else {
                    requestPermissions.launch(permissions)
                }
            }

            suspend fun insertSteps() {
                val result = try {
                    val stepsRecord = StepsRecord(
                        count = 120,
                        startTime = Instant.now().minusSeconds(120),
                        endTime = Instant.now(),
                        startZoneOffset = ZoneOffset.UTC,
                        endZoneOffset = ZoneOffset.UTC,
                    )
                    healthConnectClient.insertRecords(listOf(stepsRecord))
                    "Inserted successfully"
                } catch (e: Exception) {
                    e.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(activity, result, Toast.LENGTH_LONG).show()
                }
            }

            suspend fun insertSexualActivity() {
                val result = try {
                    val sexualActivityRecord = SexualActivityRecord(
                        time = Instant.now(),
                        zoneOffset = ZoneOffset.UTC,
                    )
                    healthConnectClient.insertRecords(listOf(sexualActivityRecord))
                    "Inserted successfully"
                } catch (e: Exception) {
                    e.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(activity, result, Toast.LENGTH_LONG).show()
                }
            }

            suspend fun readStepsByTimeRange(
                startTime: Instant,
                endTime: Instant
            ) {
                val result = try {
                    val response =
                        healthConnectClient.readRecords(
                            ReadRecordsRequest(
                                StepsRecord::class,
                                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                            )
                        )

                    "read ${response.records.count()} records"
                } catch (e: Exception) {
                    e.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(activity, result, Toast.LENGTH_LONG).show()
                }
            }

            suspend fun readSexualActivity(
                startTime: Instant,
                endTime: Instant
            ) {
                val result = try {
                    val response = healthConnectClient.readRecords(
                        ReadRecordsRequest(
                            SexualActivityRecord::class,
                            timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                        )
                    )

                    "read ${response.records.count()} records"
                } catch (e: Exception) {
                    e.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(activity, result, Toast.LENGTH_LONG).show()
                }
            }

            suspend fun aggregateSteps(
                startTime: Instant,
                endTime: Instant
            ) {
                val result = try {
                    val response = healthConnectClient.aggregate(
                        AggregateRequest(
                            metrics = setOf(StepsRecord.COUNT_TOTAL),
                            timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                        )
                    )
                    // The result may be null if no data is available in the time range
                    "step count = ${response[StepsRecord.COUNT_TOTAL].toString()}"
                } catch (e: Exception) {
                    e.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(activity, result, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private lateinit var state: State

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        state = when (HealthConnectClient.getSdkStatus(this.applicationContext)) {
            SDK_UNAVAILABLE -> State.Unavailable
            SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> State.UpdateRequired()
            SDK_AVAILABLE -> State.Available(
                activity = this,
                healthConnectClient = HealthConnectClient.getOrCreate(applicationContext)
            )

            else -> throw IllegalStateException()
        }

        enableEdgeToEdge()
        setContent {
            HealthConnectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (val currentState = state) {
                        is State.Available -> Available(
                            insertSteps = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    currentState.checkPermissionsAndRun { currentState.insertSteps() }
                                }
                            },
                            readStepsForLast24Hours = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    currentState.checkPermissionsAndRun {
                                        currentState.readStepsByTimeRange(
                                            startTime = Instant.now().minusSeconds(24 * 60 * 60),
                                            endTime = Instant.now()
                                        )
                                    }
                                }
                            },
                            aggregateStepsForLast24Hours = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    currentState.checkPermissionsAndRun {
                                        currentState.aggregateSteps(
                                            startTime = Instant.now().minusSeconds(24 * 60 * 60),
                                            endTime = Instant.now(),
                                        )
                                    }
                                }
                            },
                            insertSexualActivity = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    currentState.checkPermissionsAndRun {
                                        currentState.insertSexualActivity()
                                    }
                                }
                            },
                            readSexualActivityForLast30Days = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    currentState.checkPermissionsAndRun {
                                        currentState.readSexualActivity(
                                            startTime = Instant.now().minus(30, ChronoUnit.DAYS),
                                            endTime = Instant.now(),
                                        )
                                    }
                                }
                            },
                            readSexualActivityForLast365Days = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    currentState.checkPermissionsAndRun {
                                        currentState.readSexualActivity(
                                            startTime = Instant.now().minus(365, ChronoUnit.DAYS),
                                            endTime = Instant.now(),
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.padding(innerPadding)
                        )

                        State.Unavailable -> Unavailable(
                            modifier = Modifier.padding(innerPadding)
                        )

                        is State.UpdateRequired -> UpdateRequired(
                            onUpdateClick = {
                                startActivity(currentState.getUpdateIntent(packageName))
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
