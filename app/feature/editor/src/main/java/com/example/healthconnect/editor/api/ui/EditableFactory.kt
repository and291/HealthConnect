package com.example.healthconnect.editor.api.ui

import com.example.healthconnect.editor.api.domain.entity.Editable

interface EditableFactory {

    fun create(editable: Editable): Editable
}