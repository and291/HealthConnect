package com.example.healthconnect.editor.api.domain.record.factory

import androidx.health.connect.client.records.Record
import com.example.healthconnect.editor.api.domain.record.Model

interface ModelFactory {
    fun create(record: Record): Model
}