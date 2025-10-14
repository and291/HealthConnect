package com.example.healthconnect.utilty.impl.ui.model

import androidx.health.connect.client.records.Record

data class DisplayRecord(
    val description: String,
    val metadataId: String,
    val record: Record,
)
