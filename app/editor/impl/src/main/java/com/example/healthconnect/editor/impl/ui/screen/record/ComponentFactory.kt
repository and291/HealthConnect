package com.example.healthconnect.editor.impl.ui.screen.record

import androidx.compose.runtime.Composable
import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.components.api.ui.model.ComponentEditorModel
import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.editor.api.ui.model.BasalBodyTemperatureRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.BasalMetabolicRateRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.BloodGlucoseLevelRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent.OnDoubleValueChanged
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent.OnValueSelected
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent.OnMetadataChanged
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent.OnTimeChanged
import com.example.healthconnect.editor.api.ui.model.RecordEditorModel
import kotlin.reflect.full.declaredMemberProperties

class ComponentFactory(
    private val componentProvider: ComponentProvider,
) {

    @Composable
    fun Create(
        recordEditorModel: RecordEditorModel,
        eventHandler: (RecordModificationEvent) -> Unit,
    ) {
        val propertyValues = when (recordEditorModel) {
            is BasalBodyTemperatureRecordEditorModel -> BasalBodyTemperatureRecordEditorModel::class.declaredMemberProperties.map { it.get(recordEditorModel) as ComponentEditorModel }
            is BasalMetabolicRateRecordEditorModel -> BasalMetabolicRateRecordEditorModel::class.declaredMemberProperties.map { it.get(recordEditorModel) as ComponentEditorModel }
            is BloodGlucoseLevelRecordEditorModel -> BloodGlucoseLevelRecordEditorModel::class.declaredMemberProperties.map { it.get(recordEditorModel) as ComponentEditorModel }
        }

        propertyValues.forEach {
            Create(
                editorModel = it,
                eventHandler = eventHandler
            )
        }
    }

    @Composable
    private fun Create(
        editorModel: ComponentEditorModel,
        eventHandler: (RecordModificationEvent) -> Unit,
    ) = when (editorModel) {
        is TimeEditorModel.Valid -> componentProvider.TimeEditor(
            time = editorModel.instant,
            zoneOffset = editorModel.zoneOffset,
        ) { eventHandler(OnTimeChanged(it)) }

        is TimeEditorModel.Invalid -> TODO()

        is MetadataEditorModel -> componentProvider.MetadataEditor(editorModel) {
            eventHandler(OnMetadataChanged(it))
        }

        is DoubleValueEditorModel -> componentProvider.DoubleValueEditor(editorModel) {
            eventHandler(OnDoubleValueChanged(it))
        }

        is SelectorEditorModel -> componentProvider.Selector(editorModel) {
            eventHandler(OnValueSelected(it))
        }
    }
}