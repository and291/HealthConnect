package com.example.healthconnect.utilty.impl.ui.field


import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.domain.entity.ComponentModel
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField

@Composable
fun ComponentModel.Summary(modifier: Modifier = Modifier) {
    when (this) {
        is MetadataField -> this.Summary(modifier)
        is TimeField -> this.Summary(modifier)
        is ValueField -> this.Summary(modifier)
        is StringField -> this.Summary(modifier)
        is SelectorField -> this.Summary(modifier)
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