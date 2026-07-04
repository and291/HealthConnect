package com.example.healthconnect.integration.editor

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.health.connect.client.records.Record
import com.example.healthconnect.components.impl.di.Di as ComponentsDi
import com.example.healthconnect.editor.api.domain.entity.EditEvent
import com.example.healthconnect.editor.api.domain.entity.Editable
import com.example.healthconnect.editor.api.domain.entity.EditableField
import com.example.healthconnect.editor.api.domain.entity.Result
import com.example.healthconnect.editor.api.domain.usecase.CreateEditable
import com.example.healthconnect.editor.api.domain.usecase.GetEditable
import com.example.healthconnect.editor.api.domain.usecase.Insert
import com.example.healthconnect.editor.api.domain.usecase.Update
import com.example.healthconnect.editor.api.ui.ComponentFactory
import com.example.healthconnect.editor.api.ui.EditableFactory
import com.example.healthconnect.utilty.api.domain.entity.Result as UtilityResult
import com.example.healthconnect.utilty.api.domain.record.Model
import com.example.healthconnect.utilty.impl.di.Di as UtilityDi
import com.example.healthconnect.utilty.impl.domain.model.FieldModificationEvent
import com.example.healthconnect.utilty.impl.domain.record.factory.ModelFactory
import com.example.healthconnect.utilty.impl.ui.record.ComponentFactory as UtilityComponentFactory
import java.util.UUID
import kotlin.reflect.KClass

internal class GetEditableImpl(
    private val modelFactory: ModelFactory
) : GetEditable {
    override suspend fun invoke(uuid: UUID, kClass: KClass<*>): Editable {
        @Suppress("UNCHECKED_CAST")
        val record = UtilityDi.getEditable(uuid, kClass as KClass<out Record>)
        val model = modelFactory.create(record)
        return wrapModel(model)
    }
}

internal class CreateEditableImpl : CreateEditable {
    override fun invoke(klass: KClass<*>): Editable {
        @Suppress("UNCHECKED_CAST")
        val modelKClass = klass as KClass<out Model>
        val editor = UtilityDi.editorFactory.createByModel(modelKClass)
        val record = editor.createDefault()
        val model = UtilityDi.modelFactory.create(record)
        return ModelEditableAdapter(model, editor)
    }
}

internal class UpdateImpl : Update {
    override suspend fun invoke(modifiedRecord: Editable): Result {
        val adapter = modifiedRecord as ModelEditableAdapter
        val result = UtilityDi.update(adapter.model)
        return when (result) {
            is UtilityResult.Success -> Result.Ok
            else -> Result.Error
        }
    }
}

internal class InsertImpl : Insert {
    override fun invoke(editable: Editable): Result {
        val adapter = editable as ModelEditableAdapter
        val result = kotlinx.coroutines.runBlocking {
            UtilityDi.insert(adapter.model)
        }
        return when (result) {
            is UtilityResult.Success -> Result.Ok
            else -> Result.Error
        }
    }
}

internal class ComponentFactoryImpl : ComponentFactory {
    private val internalFactory = UtilityComponentFactory(ComponentsDi.fieldProvider)

    override fun LazyListScope.create(
        components: List<EditableField>,
        modifier: Modifier,
        eventHandler: (EditEvent) -> Unit
    ) {
        val fields = components.map { (it as FieldEditableAdapter).field }
        with(internalFactory) {
            create(fields, modifier) { internalEvent ->
                val apiEvent = when (internalEvent) {
                    is FieldModificationEvent.OnChanged ->
                        EditEvent.OnChanged(FieldEditableAdapter(internalEvent.component))
                    is FieldModificationEvent.RemoveListItem ->
                        EditEvent.RemoveListItem(internalEvent.instanceId)
                }
                eventHandler(apiEvent)
            }
        }
    }
}

internal class EditableFactoryImpl : EditableFactory {
    override fun create(editable: Editable): Editable {
        val adapter = editable as ModelEditableAdapter
        val record = UtilityDi.modelFactory.createByModel(adapter.model)
        val newModel = UtilityDi.modelFactory.create(record)
        return ModelEditableAdapter(newModel, adapter.editor)
    }
}
