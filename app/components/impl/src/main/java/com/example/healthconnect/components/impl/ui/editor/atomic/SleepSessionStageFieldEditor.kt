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
import androidx.health.connect.client.records.SleepSessionRecord
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SleepSessionStageField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import java.time.Instant

@Composable
fun SleepSessionStageFieldEditor(
    item: SleepSessionStageField,
    onChanged: (SleepSessionStageField) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Sleep Stage", style = MaterialTheme.typography.titleSmall)
            Button(onClick = onDelete) { Text("Delete") }
        }
        StringFieldEditor(
            model = item.startTime,
            onChanged = { onChanged(item.copy(startTime = it)) },
            modifier = Modifier.fillMaxWidth()
        )
        StringFieldEditor(
            model = item.endTime,
            onChanged = { onChanged(item.copy(endTime = it)) },
            modifier = Modifier.fillMaxWidth()
        )
        SelectorFieldEditor(
            editor = SelectorField(item.stage, SelectorField.Type.SleepStageType()),
            onItemSelected = { onChanged(item.copy(stage = it.value)) }
        )
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun SleepSessionStageFieldEditorPreview() {
    SleepSessionStageFieldEditor(
        item = SleepSessionStageField(
            startTime = StringField(
                value = Instant.now().toString(),
                type = StringField.Type.SleepSessionStageTime("Start Time")
            ),
            endTime = StringField(
                value = Instant.now().plusSeconds(3600).toString(),
                type = StringField.Type.SleepSessionStageTime("End Time")
            ),
            stage = SleepSessionRecord.STAGE_TYPE_DEEP
        ),
        onChanged = {},
        onDelete = {}
    )
}
