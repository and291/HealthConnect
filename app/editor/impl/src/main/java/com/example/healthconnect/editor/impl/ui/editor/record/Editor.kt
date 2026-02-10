package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.Record
import com.example.healthconnect.components.api.domain.entity.ComponentModel
import com.example.healthconnect.components.api.domain.entity.field.composite.Composite
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.editor.api.domain.model.FieldModificationEvent
import com.example.healthconnect.editor.api.domain.record.Model
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.util.UUID
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

sealed class Editor<R : Record, M : Model> {

    fun update(model: M, event: FieldModificationEvent): M = when (event) {
        is FieldModificationEvent.OnChanged -> {
            deepReflectUpdate(model, event.component.instanceId, event.component)
        }

        is FieldModificationEvent.RemoveListItem -> {
            model.getComponents().forEach { component ->
                if (component is ListField<*> && component.items.any { it.instanceId == event.instanceId }) {
                    val updatedList =  component.removeItemByPresentationId(event.instanceId)
                    return deepReflectUpdate(model, component.instanceId, updatedList)
                }
            }
            throw IllegalStateException("Item not found")
        }
    }

    abstract fun toModel(record: R, mapper: MetadataMapper): M

    abstract fun toRecord(validModel: M, mapper: MetadataMapper): R

    abstract fun createDefault(): R

    /**
     * Recursively searches for a property by presentationId and returns a new instance with the updated value.
     */
    private fun <T : Any> deepReflectUpdate(
        instance: T,
        targetId: UUID,
        newValue: ComponentModel,
    ): T {
        val kClass = instance::class

        // 1. Check if any top-level property matches the targetId
        val directProperty = kClass.memberProperties.firstOrNull { prop ->
            val value = prop.getter.call(instance)
            (value as? ComponentModel)?.instanceId == targetId
        }

        if (directProperty != null) {
            return applyCopy(instance, directProperty.name, newValue)
        }

        // 2. If not found, search recursively in child properties that are NOT ComponentModels themselves
        // (but might contain them, like MetadataComponentModel)
        for (prop in kClass.memberProperties) {
            val childValue = prop.getter.call(instance) ?: continue

            // Only recurse into objects that are NOT the newValue (to avoid infinite loops)
            // and are typical container models (not primitives or the target ComponentModel itself)
            if (childValue is Composite && childValue::class.isData) {
                val updatedChild = deepReflectUpdate(childValue, targetId, newValue)
                if (updatedChild !== childValue) {
                    return applyCopy(instance, prop.name, updatedChild)
                }
            }
        }

        return instance
    }

    private fun <T : Any> applyCopy(instance: T, propertyName: String, newValue: Any): T {
        val kClass = instance::class
        @Suppress("UNCHECKED_CAST")
        val copyFunction = kClass.memberFunctions.firstOrNull { it.name == "copy" } as? KFunction<T>
            ?: throw Exception("Copy function not found. Was it obfuscated?")

        val args = mutableMapOf<KParameter, Any?>()
        copyFunction.instanceParameter?.let { args[it] = instance }

        val parameter = copyFunction.parameters.firstOrNull { it.name == propertyName }
        if (parameter != null) {
            args[parameter] = newValue
            return try {
                copyFunction.callBy(args)
            } catch (_: Exception) {
                instance
            }
        }
        return instance
    }
}