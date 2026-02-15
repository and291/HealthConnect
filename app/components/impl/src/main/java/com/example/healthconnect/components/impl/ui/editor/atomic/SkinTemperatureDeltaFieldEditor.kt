package com.example.healthconnect.components.impl.ui.editor.atomic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthconnect.components.api.domain.entity.field.atomic.SkinTemperatureDeltaField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import java.time.Instant

@Composable
fun SkinTemperatureDeltaFieldEditor(
    item: SkinTemperatureDeltaField,
    onChanged: (SkinTemperatureDeltaField) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Temperature Delta", style = MaterialTheme.typography.titleSmall)
            Button(onClick = onDelete) { Text("Delete") }
        }
        StringFieldEditor(
            model = item.time,
            onChanged = { onChanged(item.copy(time = it)) },
            modifier = Modifier.fillMaxWidth()
        )
        ValueFieldEditor(
            model = item.delta,
            onChanged = { onChanged(item.copy(delta = it)) },
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun SkinTemperatureDeltaFieldEditorPreview() {
    SkinTemperatureDeltaFieldEditor(
        item = SkinTemperatureDeltaField(
            time = StringField(
                value = Instant.now().toString(),
                type = StringField.Type.SkinTemperatureDeltaTime()
            ),
            delta = ValueField.Dbl(
                parsedValue = 0.5,
                type = ValueField.Type.TemperatureDelta()
            )
        ),
        onChanged = {},
        onDelete = {}
    )
}
