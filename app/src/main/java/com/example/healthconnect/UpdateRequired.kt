package com.example.healthconnect

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthconnect.ui.theme.HealthConnectTheme


@Composable
fun UpdateRequired(
    onUpdateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Health Connect SDK update required")
        Button(onClick = onUpdateClick) {
            Text(text = "Update")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateRequiredPreview() {
    HealthConnectTheme {
        UpdateRequired({})
    }
}
