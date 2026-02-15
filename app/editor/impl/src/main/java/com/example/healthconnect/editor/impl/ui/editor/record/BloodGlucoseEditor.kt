package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.BloodGlucose
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.editor.api.domain.record.BloodGlucoseLevel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class BloodGlucoseEditor() : Editor<BloodGlucoseRecord, BloodGlucoseLevel>() {

    override fun toModel(
        record: BloodGlucoseRecord,
        mapper: MetadataMapper,
    ): BloodGlucoseLevel = BloodGlucoseLevel(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        level = ValueField.Dbl(
            parsedValue = record.level.inMillimolesPerLiter,
            type = ValueField.Type.BloodGlucoseLevel(),
        ),
        specimenSource = SelectorField(
            value = record.specimenSource,
            type = SelectorField.Type.SpecimenSource()
        ),
        mealType = SelectorField(
            value = record.mealType,
            type = SelectorField.Type.BloodGlucoseMealType()
        ),
        relationToMeals = SelectorField(
            value = record.relationToMeal,
            type = SelectorField.Type.RelationToMeal()
        ),
    )

    override fun toRecord(
        validModel: BloodGlucoseLevel,
        mapper: MetadataMapper,
    ): BloodGlucoseRecord = BloodGlucoseRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        level = BloodGlucose.millimolesPerLiter((validModel.level as ValueField.Dbl).parsedValue!!),
        specimenSource = validModel.specimenSource.value,
        mealType = validModel.mealType.value,
        relationToMeal = validModel.relationToMeals.value,
    )

    override fun createDefault(): BloodGlucoseRecord = BloodGlucoseRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        level = BloodGlucose.millimolesPerLiter(5.0)
    )
}
