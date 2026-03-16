package com.example.healthconnect.utilty.impl.ui.field

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.editor.api.domain.record.Model

@Composable
fun Model.Summary(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        getComponents().forEach { it.Summary() }
    }
}