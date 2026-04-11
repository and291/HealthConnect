package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.Record
import com.example.healthconnect.components.api.domain.entity.Field
import com.example.healthconnect.components.api.domain.entity.field.composite.Composite
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.editor.api.domain.model.FieldModificationEvent
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.models.api.domain.record.Model
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
            // Find the list that contains the ID and return the updated version of that list
            val updatedComponentsList = model.getFields()
                .filterIsInstance<ListField<*>>()
                .firstNotNullOfOrNull { listField ->
                    listField.findAndRemoveFromList(event.instanceId)
                } ?: throw IllegalStateException("Item with ID ${event.instanceId} not found")

            deepReflectUpdate(model, updatedComponentsList.instanceId, updatedComponentsList)
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
        newValue: Field,
    ): T {
        val kClass = instance::class

        // 1. Check if any top-level property matches the targetId
        kClass.memberProperties.forEach { prop ->
            val value = prop.getter.call(instance)
            if ((value as? Field)?.instanceId == targetId) {
                return applyCopy(instance, prop.name, newValue)
            }
            //check content of Composite components
            if (value is Composite && value.containsInstanceId(targetId)) {
                @Suppress("UNCHECKED_CAST")
                val updated = value.updateFieldByInstanceId(targetId, newValue) as T
                return applyCopy(instance, prop.name, updated)
            }
        }

        // 2. Check each nested list
        kClass.memberProperties.forEach { prop ->
            val value = prop.getter.call(instance)
            if (value is ListField<*>) {
                value.items.forEach { itemComponent ->
                    val updatedComponent = deepReflectUpdate(itemComponent, targetId, newValue)
                    if (updatedComponent !== itemComponent) {
                        val updatedList = value.updateFieldByInstanceId(
                            updatedComponent.instanceId,
                            updatedComponent
                        )
                        return applyCopy(instance, prop.name, updatedList)
                    }
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