package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.BloodGlucose
import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.BloodGlucoseModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class BloodGlucoseEditor() : Editor<BloodGlucoseRecord, BloodGlucoseModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: BloodGlucoseModel,
        event: ModelModificationEvent,
    ): BloodGlucoseModel = when (event) {
        is ModelModificationEvent.OnValueSelected -> when (event.selector.type) {
            is SelectorComponentModel.Type.MealType -> model.copy(
                mealType = event.selector,
            )

            is SelectorComponentModel.Type.RelationToMeal -> model.copy(
                relationToMeals = event.selector,
            )

            is SelectorComponentModel.Type.SpecimenSource -> model.copy(
                specimenSource = event.selector,
            )

            else -> throw NotImplementedError()
        }

        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.BloodGlucoseLevel -> model.copy(
                level = event.value
            )

            else -> throw NotImplementedError()
        }

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: BloodGlucoseRecord,
        mapper: MetadataMapper,
    ): BloodGlucoseModel = BloodGlucoseModel(
        time = TimeComponentModel.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        level = ValueComponentModel.Dbl(
            parsedValue = record.level.inMillimolesPerLiter,
            type = ValueComponentModel.Type.BloodGlucoseLevel(),
        ),
        specimenSource = SelectorComponentModel(
            value = record.specimenSource,
            type = SelectorComponentModel.Type.SpecimenSource()
        ),
        mealType = SelectorComponentModel(
            value = record.mealType,
            type = SelectorComponentModel.Type.MealType()
        ),
        relationToMeals = SelectorComponentModel(
            value = record.relationToMeal,
            type = SelectorComponentModel.Type.RelationToMeal()
        ),
    )

    override fun toRecord(
        validModel: BloodGlucoseModel,
        mapper: MetadataMapper,
    ): BloodGlucoseRecord = BloodGlucoseRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        level = BloodGlucose.Companion.millimolesPerLiter((validModel.level as ValueComponentModel.Dbl).parsedValue!!),
        specimenSource = validModel.specimenSource.value,
        mealType = validModel.mealType.value,
        relationToMeal = validModel.relationToMeals.value,
    )

    override fun createDefault(): BloodGlucoseRecord = BloodGlucoseRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        level = BloodGlucose.Companion.millimolesPerLiter(5.0)
    )
}