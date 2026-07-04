package com.example.healthconnect.integration.editor

import com.example.healthconnect.components.api.domain.entity.Field
import com.example.healthconnect.editor.api.domain.entity.EditEvent
import com.example.healthconnect.editor.api.domain.entity.Editable
import com.example.healthconnect.editor.api.domain.entity.EditableField
import com.example.healthconnect.utilty.api.record.Model
import java.util.UUID
import androidx.health.connect.client.records.Record
import com.example.healthconnect.utilty.impl.domain.model.FieldModificationEvent
import com.example.healthconnect.utilty.impl.impl.ui.editor.record.Editor

class ModelEditableAdapter(
    val model: Model,
    val editor: Editor<Record, Model>
) : Editable {
    override fun getFields(): List<EditableField> = model.getFields().map { FieldEditableAdapter(it) }

    override fun update(field: Editable, event: EditEvent): Editable {
        val currentModel = (field as ModelEditableAdapter).model
        val internalEvent = when (event) {
            is EditEvent.OnChanged -> FieldModificationEvent.OnChanged((event.component as FieldEditableAdapter).field)
            is EditEvent.RemoveListItem -> FieldModificationEvent.RemoveListItem(event.instanceId)
        }
        val updatedModel = editor.update(currentModel, internalEvent)
        val updatedEditable = ModelEditableAdapter(updatedModel, editor)
        return updatedEditable
    }

    override fun isValid(): Boolean = model.isValid()

    override fun getUuid(): UUID = UUID.fromString(model.metadata.id.value) //TODO fix
}

class FieldEditableAdapter(val field: Field) : EditableField {
    override val priority: Int = field.priority
}

fun wrapModel(model: Model): Editable {
    val editor = com.example.healthconnect.utilty.impl.di.Di.editorFactory.createByModel(model::class)
    val editable = ModelEditableAdapter(model, editor)
    return editable
}
