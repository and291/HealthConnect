package com.example.healthconnect.editor.impl.ui.screen.record

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.components.api.ui.model.ComponentEditorModel
import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.editor.api.ui.model.BasalBodyTemperatureRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.BasalMetabolicRateRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.BloodGlucoseLevelRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.BloodPressureRecordEditorModel
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
        modifier: Modifier = Modifier,
        eventHandler: (RecordModificationEvent) -> Unit,
    ) {
        val propertyValues = when (recordEditorModel) {
            is BasalBodyTemperatureRecordEditorModel -> BasalBodyTemperatureRecordEditorModel::class.declaredMemberProperties.map { it.get(recordEditorModel) as ComponentEditorModel }
            is BasalMetabolicRateRecordEditorModel -> BasalMetabolicRateRecordEditorModel::class.declaredMemberProperties.map { it.get(recordEditorModel) as ComponentEditorModel }
            is BloodGlucoseLevelRecordEditorModel -> BloodGlucoseLevelRecordEditorModel::class.declaredMemberProperties.map { it.get(recordEditorModel) as ComponentEditorModel }
            is BloodPressureRecordEditorModel -> BloodPressureRecordEditorModel::class.declaredMemberProperties.map { it.get(recordEditorModel) as ComponentEditorModel }
        }

        propertyValues.forEach {
            Create(
                editorModel = it,
                eventHandler = eventHandler,
                modifier = modifier,
            )
        }
    }

    @Composable
    private fun Create(
        editorModel: ComponentEditorModel,
        eventHandler: (RecordModificationEvent) -> Unit,
        modifier: Modifier,
    ) = when (editorModel) {
        is TimeEditorModel.Valid -> componentProvider.TimeEditor(
            time = editorModel.instant,
            zoneOffset = editorModel.zoneOffset,
            modifier = modifier,
        ) { eventHandler(OnTimeChanged(it)) }

        is TimeEditorModel.Invalid -> TODO()

        is MetadataEditorModel -> componentProvider.MetadataEditor(editorModel, modifier) {
            eventHandler(OnMetadataChanged(it))
        }

        is DoubleValueEditorModel -> componentProvider.DoubleValueEditor(editorModel, modifier) {
            eventHandler(OnDoubleValueChanged(it))
        }

        is SelectorEditorModel -> componentProvider.Selector(editorModel, modifier) {
            eventHandler(OnValueSelected(it))
        }
    }
}