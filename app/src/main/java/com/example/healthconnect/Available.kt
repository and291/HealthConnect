package com.example.healthconnect

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthconnect.ui.theme.HealthConnectTheme


@Composable
fun Available(
    insertSteps: () -> Unit,
    readStepsForLast24Hours: () -> Unit,
    aggregateStepsForLast24Hours: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Health Connect SDK is Available")
        Button(onClick = insertSteps) {
            Text(text = "Insert 120 steps for the last 2 minutes")
        }
        Button(onClick = readStepsForLast24Hours) {
            Text(text = "Read steps for last 24 hours")
        }
        Button(onClick = aggregateStepsForLast24Hours) {
            Text(text = "Aggregate steps for last 24 hours")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AvailablePreview() {
    HealthConnectTheme {
        Available({}, {}, {})
    }
}
