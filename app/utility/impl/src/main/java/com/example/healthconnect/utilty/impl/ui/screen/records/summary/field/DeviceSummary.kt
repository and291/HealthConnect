package com.example.healthconnect.utilty.impl.ui.screen.records.summary.field

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.connect.client.records.metadata.Device
import com.example.healthconnect.components.api.domain.entity.field.atomic.DeviceField

@Composable
@Preview(showBackground = true)
private fun DeviceFieldSpecifiedPreview() {
    DeviceField.Specified(
        type = Device.TYPE_WATCH,
        manufacturer = "Google",
        model = "Pixel Watch",
    ).Summary()
}

@Composable
@Preview(showBackground = true)
private fun DeviceFieldEmptyPreview() {
    DeviceField.Empty().Summary()
}

@Composable
fun DeviceField.Summary(modifier: Modifier = Modifier) {
    when (this) {
        is DeviceField.Empty -> Unit
        is DeviceField.Specified -> {
            Row(modifier = modifier) {
                Text(
                    text = "Device: ",
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = "${mapType(type)} $manufacturer $model".trim(),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

private fun mapType(type: Int): String = when (type) {
    Device.TYPE_WATCH -> "Watch"
    Device.TYPE_PHONE -> "Phone"
    Device.TYPE_SCALE -> "Scale"
    Device.TYPE_RING -> "Ring"
    Device.TYPE_HEAD_MOUNTED -> "Head Mounted"
    Device.TYPE_FITNESS_BAND -> "Fitness Band"
    Device.TYPE_CHEST_STRAP -> "Chest Strap"
    Device.TYPE_SMART_DISPLAY -> "Smart Display"
    Device.TYPE_CONSUMER_MEDICAL_DEVICE -> "Consumer Medical Device"
    Device.TYPE_GLASSES -> "Glasses"
    Device.TYPE_HEARABLE -> "Hearable"
    Device.TYPE_FITNESS_MACHINE -> "Fitness Machine"
    Device.TYPE_FITNESS_EQUIPMENT -> "Fitness Equipment"
    Device.TYPE_PORTABLE_COMPUTER -> "Portable Computer"
    Device.TYPE_METER -> "Meter"
    else -> "Unknown"
}
