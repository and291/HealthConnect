package com.example.healthconnect.editor.di

import com.example.healthconnect.editor.api.domain.usecase.CreateEditable
import com.example.healthconnect.editor.api.domain.usecase.GetEditable
import com.example.healthconnect.editor.api.domain.usecase.Insert
import com.example.healthconnect.editor.api.domain.usecase.Update
import com.example.healthconnect.editor.api.ui.ComponentFactory
import com.example.healthconnect.editor.api.ui.EditableFactory

internal sealed interface Dependencies {

    val getEditable: GetEditable
    val update: Update
    val createEditable: CreateEditable
    val insert: Insert
    val componentFactory: ComponentFactory
    val editableFactory: EditableFactory
}

data class ProductionDependencies (
    override val getEditable: GetEditable,
    override val createEditable: CreateEditable,
    override val update: Update,
    override val insert: Insert,
    override val componentFactory: ComponentFactory,
    override val editableFactory: EditableFactory,
) : Dependencies