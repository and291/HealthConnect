package com.example.healthconnect.editor.api.ui.mapper

import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.units.BloodGlucose
import androidx.health.connect.client.units.Pressure
import androidx.health.connect.client.units.celsius
import androidx.health.connect.client.units.kilocaloriesPerDay
import androidx.health.connect.client.units.percent
import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.editor.api.ui.model.BasalBodyTemperatureRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.BasalMetabolicRateRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.BloodGlucoseLevelRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.BloodPressureRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.BodyFatRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.BodyTemperatureRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.RecordEditorModel

class RecordMapper(
    private val metadataMapper: MetadataMapper
) {

    fun toUiModel(record: Record): RecordEditorModel = when (record) {
        is BasalBodyTemperatureRecord -> BasalBodyTemperatureRecordEditorModel(
            time = TimeEditorModel.Valid(
                instant = record.time,
                zoneOffset = record.zoneOffset
            ),
            metadata = metadataMapper.toEntity(record.metadata),
            temperature = DoubleValueEditorModel.Valid(
                parsedValue = record.temperature.inCelsius,
                type = DoubleValueEditorModel.Type.Temperature(),
            ),
            measurementLocation = SelectorEditorModel.Valid(
                value = record.measurementLocation, //TODO validate data from lib
                type = SelectorEditorModel.Type.MeasurementLocationBodyTemperature(),
            )
        )

        is BodyTemperatureRecord -> BodyTemperatureRecordEditorModel(
            time = TimeEditorModel.Valid(
                instant = record.time,
                zoneOffset = record.zoneOffset
            ),
            metadata = metadataMapper.toEntity(record.metadata),
            temperature = DoubleValueEditorModel.Valid(
                parsedValue = record.temperature.inCelsius,
                type = DoubleValueEditorModel.Type.Temperature(),
            ),
            measurementLocation = SelectorEditorModel.Valid(
                value = record.measurementLocation, //TODO validate data from lib
                type = SelectorEditorModel.Type.MeasurementLocationBodyTemperature(),
            )
        )

        is BasalMetabolicRateRecord -> BasalMetabolicRateRecordEditorModel(
            time = TimeEditorModel.Valid(
                instant = record.time,
                zoneOffset = record.zoneOffset
            ),
            metadata = metadataMapper.toEntity(record.metadata),
            power = DoubleValueEditorModel.Valid(
                parsedValue = record.basalMetabolicRate.inKilocaloriesPerDay,
                type = DoubleValueEditorModel.Type.Power(),
            )
        )

        is BloodGlucoseRecord -> BloodGlucoseLevelRecordEditorModel(
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
        
        is BloodPressureRecord -> BloodPressureRecordEditorModel(
            time = TimeEditorModel.Valid(
                instant = record.time,
                zoneOffset = record.zoneOffset
            ),
            metadata = metadataMapper.toEntity(record.metadata),
            systolic = DoubleValueEditorModel.Valid(
                parsedValue = record.systolic.inMillimetersOfMercury,
                type = DoubleValueEditorModel.Type.SystolicPressure()
            ),
            diastolic = DoubleValueEditorModel.Valid(
                parsedValue = record.diastolic.inMillimetersOfMercury,
                type = DoubleValueEditorModel.Type.DiastolicPressure()
            ),
            bodyPosition = SelectorEditorModel.Valid(
                value = record.bodyPosition,
                type = SelectorEditorModel.Type.BodyPosition()
            ),
            measurementLocation = SelectorEditorModel.Valid(
                value = record.measurementLocation,
                type = SelectorEditorModel.Type.MeasurementLocationBloodPressure()
            )
        )

        is BodyFatRecord -> BodyFatRecordEditorModel(
            timeEditorModel = TimeEditorModel.Valid(
                instant = record.time,
                zoneOffset = record.zoneOffset
            ),
            metadata = metadataMapper.toEntity(record.metadata),
            percentage = DoubleValueEditorModel.Valid(
                parsedValue = record.percentage.value,
                type = DoubleValueEditorModel.Type.PercentageBodyFat(),
            )
        )

        else -> TODO()
    }

    /**
     * Creates new Library Entity based on VALID EditorModel
     * @throws error if parameter is invalid
     * @throws error if Library Entity's validation rules (checked at instance creation time) are not satisfied
     */
    @Throws(Exception::class)
    fun toEntity(validUiModel: RecordEditorModel): Record = when (validUiModel) {
        is BasalBodyTemperatureRecordEditorModel -> BasalBodyTemperatureRecord(
            time = (validUiModel.time as TimeEditorModel.Valid).instant,
            zoneOffset = validUiModel.time.zoneOffset,
            metadata = metadataMapper.toLibMetadata(validUiModel.metadata),
            temperature = (validUiModel.temperature as DoubleValueEditorModel.Valid).parsedValue.celsius,
            measurementLocation = (validUiModel.measurementLocation as SelectorEditorModel.Valid).value
        )

        is BodyTemperatureRecordEditorModel -> BodyTemperatureRecord(
            time = (validUiModel.time as TimeEditorModel.Valid).instant,
            zoneOffset = validUiModel.time.zoneOffset,
            metadata = metadataMapper.toLibMetadata(validUiModel.metadata),
            temperature = (validUiModel.temperature as DoubleValueEditorModel.Valid).parsedValue.celsius,
            measurementLocation = (validUiModel.measurementLocation as SelectorEditorModel.Valid).value
        )

        is BasalMetabolicRateRecordEditorModel -> BasalMetabolicRateRecord(
            time = (validUiModel.time as TimeEditorModel.Valid).instant,
            zoneOffset = validUiModel.time.zoneOffset,
            metadata = metadataMapper.toLibMetadata(validUiModel.metadata),
            basalMetabolicRate = (validUiModel.power as DoubleValueEditorModel.Valid).parsedValue.kilocaloriesPerDay,
        )

        is BloodGlucoseLevelRecordEditorModel -> BloodGlucoseRecord(
            time = (validUiModel.time as TimeEditorModel.Valid).instant,
            zoneOffset = validUiModel.time.zoneOffset,
            metadata = metadataMapper.toLibMetadata(validUiModel.metadata),
            level = BloodGlucose.millimolesPerLiter((validUiModel.level as DoubleValueEditorModel.Valid).parsedValue),
            specimenSource = (validUiModel.specimenSource as SelectorEditorModel.Valid).value,
            mealType = (validUiModel.mealType as SelectorEditorModel.Valid).value,
            relationToMeal = (validUiModel.relationToMeals as SelectorEditorModel.Valid).value
        )

        is BloodPressureRecordEditorModel -> BloodPressureRecord(
            time = (validUiModel.time as TimeEditorModel.Valid).instant,
            zoneOffset = validUiModel.time.zoneOffset,
            metadata = metadataMapper.toLibMetadata(validUiModel.metadata),
            systolic = Pressure.millimetersOfMercury((validUiModel.systolic as DoubleValueEditorModel.Valid).parsedValue),
            diastolic = Pressure.millimetersOfMercury((validUiModel.diastolic as DoubleValueEditorModel.Valid).parsedValue),
            bodyPosition = (validUiModel.bodyPosition as SelectorEditorModel.Valid).value,
            measurementLocation = (validUiModel.measurementLocation as SelectorEditorModel.Valid).value,
        )

        is BodyFatRecordEditorModel -> BodyFatRecord(
            time = (validUiModel.timeEditorModel as TimeEditorModel.Valid).instant,
            zoneOffset = validUiModel.timeEditorModel.zoneOffset,
            metadata = metadataMapper.toLibMetadata(validUiModel.metadata),
            percentage = (validUiModel.percentage as DoubleValueEditorModel.Valid).parsedValue.percent,
        )
    }
}