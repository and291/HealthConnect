package com.example.healthconnect

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthconnect.ui.theme.HealthConnectTheme


@Composable
fun Unavailable(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Health Connect SDK is Unavailable")
        Text(text = "Looks like there is nothing we can do about it")
    }
}

@Preview(showBackground = true)
@Composable
fun UnavailablePreview() {
    HealthConnectTheme {
        Unavailable()
    }
}
