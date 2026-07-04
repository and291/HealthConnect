package com.example.healthconnect.utilty.impl.domain.record.factory

import androidx.health.connect.client.records.Record
import com.example.healthconnect.utilty.api.domain.record.Model

interface ModelFactory {
    fun create(record: Record): Model
    fun createByModel(model: Model): Record
}