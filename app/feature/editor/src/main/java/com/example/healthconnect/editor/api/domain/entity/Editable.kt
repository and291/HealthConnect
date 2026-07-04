package com.example.healthconnect.editor.api.domain.entity

import java.util.UUID

interface Editable {

    fun getUuid(): UUID

    fun getFields(): List<EditableField>

    fun update(field: Editable, event: EditEvent): Editable

    fun isValid(): Boolean
}


