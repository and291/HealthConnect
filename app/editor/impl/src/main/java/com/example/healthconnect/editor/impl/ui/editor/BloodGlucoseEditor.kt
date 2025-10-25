package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.BloodGlucose
import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.BloodGlucoseLevelRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class BloodGlucoseEditor() : Editor<BloodGlucoseRecord, BloodGlucoseLevelRecordEditorModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: BloodGlucoseLevelRecordEditorModel,
        event: RecordModificationEvent,
    ): BloodGlucoseLevelRecordEditorModel = when (event) {
        is RecordModificationEvent.OnValueSelected -> when (event.selector.type) {
            is SelectorEditorModel.Type.MealType -> model.copy(
                mealType = event.selector,
            )

            is SelectorEditorModel.Type.RelationToMeal -> model.copy(
                relationToMeals = event.selector,
            )

            is SelectorEditorModel.Type.SpecimenSource -> model.copy(
                specimenSource = event.selector,
            )

            else -> throw NotImplementedError()
        }

        is RecordModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is RecordModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueEditorModel.Type.BloodGlucoseLevel -> model.copy(
                level = event.value
            )

            else -> throw NotImplementedError()
        }

        is RecordModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: BloodGlucoseRecord,
        metadataMapper: MetadataMapper,
    ): BloodGlucoseLevelRecordEditorModel = BloodGlucoseLevelRecordEditorModel(
        time = TimeEditorModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = metadataMapper.toEntity(record.metadata),
        level = DoubleValueEditorModel.Valid(
            parsedValue = record.level.inMillimolesPerLiter,
            type = DoubleValueEditorModel.Type.BloodGlucoseLevel(),
        ),
        specimenSource = SelectorEditorModel.Valid(
            value = record.specimenSource,
            type = SelectorEditorModel.Type.SpecimenSource()
        ),
        mealType = SelectorEditorModel.Valid(
            value = record.mealType,
            type = SelectorEditorModel.Type.MealType()
        ),
        relationToMeals = SelectorEditorModel.Valid(
            value = record.relationToMeal,
            type = SelectorEditorModel.Type.RelationToMeal()
        ),
    )

    override fun toRecord(
        validUiModel: BloodGlucoseLevelRecordEditorModel,
        metadataMapper: MetadataMapper,
    ): BloodGlucoseRecord = BloodGlucoseRecord(
        time = (validUiModel.time as TimeEditorModel.Valid).instant,
        zoneOffset = (validUiModel.time as TimeEditorModel.Valid).zoneOffset,
        metadata = metadataMapper.toLibMetadata(validUiModel.metadata),
        level = BloodGlucose.Companion.millimolesPerLiter((validUiModel.level as DoubleValueEditorModel.Valid).parsedValue),
        specimenSource = (validUiModel.specimenSource as SelectorEditorModel.Valid).value,
        mealType = (validUiModel.mealType as SelectorEditorModel.Valid).value,
        relationToMeal = (validUiModel.relationToMeals as SelectorEditorModel.Valid).value
    )

    override fun createDefault(): BloodGlucoseRecord = BloodGlucoseRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        level = BloodGlucose.Companion.millimolesPerLiter(5.0)
    )
}