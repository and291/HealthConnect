package com.example.healthconnect.integration.editor

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.health.connect.client.records.Record
import com.example.healthconnect.editor.api.domain.entity.EditEvent
import com.example.healthconnect.editor.api.domain.entity.Editable
import com.example.healthconnect.editor.api.domain.entity.EditableField
import com.example.healthconnect.editor.api.domain.entity.Result
import com.example.healthconnect.editor.api.domain.record.factory.ModelFactory
import com.example.healthconnect.editor.api.domain.usecase.CreateEditable
import com.example.healthconnect.editor.api.domain.usecase.GetEditable
import com.example.healthconnect.editor.api.domain.usecase.Insert
import com.example.healthconnect.editor.api.domain.usecase.Update
import com.example.healthconnect.editor.api.ui.ComponentFactory
import com.example.healthconnect.editor.api.ui.EditableFactory
import com.example.healthconnect.editor.impl.di.Di as EditorDi
import com.example.healthconnect.utilty.impl.di.Di as UtilityDi
import com.example.healthconnect.components.impl.di.Di as ComponentsDi
import com.example.healthconnect.models.api.domain.record.Model
import java.util.UUID
import kotlin.reflect.KClass

class GetEditableImpl(
    private val modelFactory: ModelFactory
) : GetEditable {
    override suspend fun invoke(uuid: UUID, kClass: KClass<*>): Editable {
        @Suppress("UNCHECKED_CAST")
        val record = UtilityDi.getEditable(uuid, kClass as KClass<out Record>)
        val model = modelFactory.create(record)
        return wrapModel(model)
    }
}

class CreateEditableImpl : CreateEditable {
    override fun invoke(klass: KClass<*>): Editable {
        @Suppress("UNCHECKED_CAST")
        val modelKClass = klass as KClass<out Model>
        val editor = EditorDi.editorFactory.createByModel(modelKClass)
        val record = editor.createDefault()
        val model = EditorDi.modelFactory.create(record)
        return ModelEditableAdapter(model, editor)
    }
}

class UpdateImpl : Update {
    override suspend fun invoke(modifiedRecord: Editable): Result {
        val adapter = modifiedRecord as ModelEditableAdapter
        val result = UtilityDi.update(adapter.model)
        return when (result) {
            is com.example.healthconnect.utilty.api.domain.entity.Result.Success -> Result.Ok
            else -> Result.Error
        }
    }
}

class InsertImpl : Insert {
    override fun invoke(editable: Editable): Result {
        val adapter = editable as ModelEditableAdapter
        val result = kotlinx.coroutines.runBlocking {
            UtilityDi.insert(adapter.model)
        }
        return when (result) {
            is com.example.healthconnect.utilty.api.domain.entity.Result.Success -> Result.Ok
            else -> Result.Error
        }
    }
}

class ComponentFactoryImpl : ComponentFactory {
    private val internalFactory = com.example.healthconnect.editor.impl.ui.screen.record.ComponentFactory(ComponentsDi.fieldProvider)

    override fun LazyListScope.create(
        components: List<EditableField>,
        modifier: Modifier,
        eventHandler: (EditEvent) -> Unit
    ) {
        val fields = components.map { (it as FieldEditableAdapter).field }
        with(internalFactory) {
            create(fields, modifier) { internalEvent ->
                val apiEvent = when (internalEvent) {
                    is com.example.healthconnect.editor.api.domain.model.FieldModificationEvent.OnChanged -> 
                        EditEvent.OnChanged(FieldEditableAdapter(internalEvent.component))
                    is com.example.healthconnect.editor.api.domain.model.FieldModificationEvent.RemoveListItem -> 
                        EditEvent.RemoveListItem(internalEvent.instanceId)
                }
                eventHandler(apiEvent)
            }
        }
    }
}

class EditableFactoryImpl : EditableFactory {
    override fun create(editable: Editable): Editable {
        val adapter = editable as ModelEditableAdapter
        val record = EditorDi.modelFactory.createByModel(adapter.model)
        val newModel = EditorDi.modelFactory.create(record)
        return ModelEditableAdapter(newModel, adapter.editor)
    }
}
