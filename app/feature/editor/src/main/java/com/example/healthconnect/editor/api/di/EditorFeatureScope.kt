package com.example.healthconnect.editor.api.di

import com.example.healthconnect.editor.api.domain.usecase.CreateEditable
import com.example.healthconnect.editor.api.domain.usecase.GetEditable
import com.example.healthconnect.editor.api.domain.usecase.Insert
import com.example.healthconnect.editor.api.domain.usecase.Update
import com.example.healthconnect.editor.api.ui.ComponentFactory
import com.example.healthconnect.editor.api.ui.EditableFactory
import com.example.healthconnect.editor.di.Locator
import com.example.healthconnect.editor.di.ProductionDependencies
import java.io.Closeable

class EditorFeatureScope(
    val getEditable: GetEditable,
    val createEditable: CreateEditable,
    val update: Update,
    val insert: Insert,
    val componentFactory: ComponentFactory,
    val editableFactory: EditableFactory,
) : Closeable {

    fun init() {
        Locator.impl = ProductionDependencies(
            getEditable = getEditable,
            createEditable = createEditable,
            update = update,
            insert = insert,
            componentFactory = componentFactory,
            editableFactory = editableFactory,
        )
    }

    override fun close() {
        Locator.clear()
    }
}