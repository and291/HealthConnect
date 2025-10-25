package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.Record
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.Model
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent

sealed class Editor<R : Record, M : Model> {

    abstract fun update(model: M, event: ModelModificationEvent): M

    abstract fun toModel(record: R, mapper: MetadataMapper): M

    abstract fun toRecord(validModel: M, mapper: MetadataMapper): R

    abstract fun createDefault(): R
}

