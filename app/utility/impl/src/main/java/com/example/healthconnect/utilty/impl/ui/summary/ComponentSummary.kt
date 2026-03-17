package com.example.healthconnect.utilty.impl.ui.summary

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthconnect.components.api.domain.entity.ComponentModel
import com.example.healthconnect.components.api.domain.entity.field.atomic.DeviceField
import com.example.healthconnect.components.api.domain.entity.field.atomic.HeartRateSampleField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import java.time.Instant

@Composable
fun ComponentModel.Summary(modifier: Modifier = Modifier) {
    when (this) {
        is MetadataField -> this.Summary(modifier)
        is TimeField -> this.Summary(modifier)
        is ValueField -> this.Summary(modifier)
        is StringField -> this.Summary(modifier)
        is SelectorField -> this.Summary(modifier)
        is DeviceField -> this.Summary(modifier)
        is ListField<*> -> Row(modifier = modifier) {
            Text(
                text = "${config.label}: ",
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                text = "${items.size} items",
                style = MaterialTheme.typography.bodySmall,
            )
        }
        else -> throw NotImplementedError("No Summary for ${this::class.simpleName}")
    }
}

@Composable
@Preview(showBackground = true)
private fun ComponentSummaryValueFieldPreview() {
    (ValueField.Dbl(parsedValue = 70.5, type = ValueField.Type.Mass()) as ComponentModel).Summary()
}

@Composable
@Preview(showBackground = true)
private fun ComponentSummaryListFieldPreview() {
    (ListField(
        items = listOf(
            HeartRateSampleField(time = Instant.parse("2024-01-15T09:00:00Z"), heartRate = ValueField.Lng(72L, ValueField.Type.BeatsPerMinute())),
            HeartRateSampleField(time = Instant.parse("2024-01-15T09:01:00Z"), heartRate = ValueField.Lng(75L, ValueField.Type.BeatsPerMinute())),
        ),
        type = ListField.Type.HeartRateSamples,
    ) as ComponentModel).Summary()
}