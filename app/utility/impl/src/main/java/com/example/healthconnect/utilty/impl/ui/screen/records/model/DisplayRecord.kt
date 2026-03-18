package com.example.healthconnect.utilty.impl.ui.screen.records.model

import androidx.health.connect.client.records.Record
import com.example.healthconnect.editor.api.domain.record.Model

data class DisplayRecord(
    val model: Model,
    val record: Record,
)