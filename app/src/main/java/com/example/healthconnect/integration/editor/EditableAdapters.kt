package com.example.healthconnect.integration.editor

import com.example.healthconnect.components.api.domain.entity.Field
import com.example.healthconnect.editor.api.domain.entity.EditEvent
import com.example.healthconnect.editor.api.domain.entity.Editable
import com.example.healthconnect.editor.api.domain.entity.EditableField
import com.example.healthconnect.utilty.api.domain.record.Model
import com.example.healthconnect.utilty.impl.di.Di as UtilityDi
import com.example.healthconnect.utilty.impl.domain.model.FieldModificationEvent
import com.example.healthconnect.utilty.impl.ui.editor.record.Editor
import java.util.UUID
import androidx.health.connect.client.records.Record

internal class ModelEditableAdapter(
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

internal class FieldEditableAdapter(val field: Field) : EditableField {
    override val priority: Int = field.priority
}

internal fun wrapModel(model: Model): Editable {
    val editor = UtilityDi.editorFactory.createByModel(model::class)
    val editable = ModelEditableAdapter(model, editor)
    return editable
}
