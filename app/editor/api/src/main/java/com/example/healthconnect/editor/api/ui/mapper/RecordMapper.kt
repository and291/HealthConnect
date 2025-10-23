package com.example.healthconnect.editor.api.ui.mapper

import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.units.BloodGlucose
import androidx.health.connect.client.units.celsius
import androidx.health.connect.client.units.kilocaloriesPerDay
import com.example.healthconnect.components.api.ui.model.BloodGlucoseLevelEditorModel
import com.example.healthconnect.components.api.ui.model.PowerEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorType
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.editor.api.ui.model.BasalBodyTemperatureRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.BasalMetabolicRateRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.BloodGlucoseLevelRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.RecordEditorModel

class RecordMapper(
    private val metadataMapper: MetadataMapper
) {

    fun toUiModel(record: Record): RecordEditorModel = when (record) {
        is BasalBodyTemperatureRecord -> BasalBodyTemperatureRecordEditorModel(
            timeEditorModel = TimeEditorModel.Valid(
                instant = record.time,
                zoneOffset = record.zoneOffset
            ),
            metadataEditorModel = metadataMapper.toEntity(record.metadata),
            temperatureEditorModel = TemperatureEditorModel.Valid(parsedValue = record.temperature.inCelsius),
            measurementLocation = SelectorEditorModel.Valid(
                value = record.measurementLocation, //TODO validate data from lib
                selectorType = SelectorType.BodyTemperatureMeasurementLocationType(),
            )
        )

        is BasalMetabolicRateRecord -> BasalMetabolicRateRecordEditorModel(
            timeEditorModel = TimeEditorModel.Valid(
                instant = record.time,
                zoneOffset = record.zoneOffset
            ),
            metadataEditorModel = metadataMapper.toEntity(record.metadata),
            powerEditorModel = PowerEditorModel.Valid(parsedValue = record.basalMetabolicRate.inKilocaloriesPerDay)
        )

        is BloodGlucoseRecord -> BloodGlucoseLevelRecordEditorModel(
            timeEditorModel = TimeEditorModel.Valid(
                instant = record.time,
                zoneOffset = record.zoneOffset
            ),
            metadataEditorModel = metadataMapper.toEntity(record.metadata),
            level = BloodGlucoseLevelEditorModel.Valid(record.level.inMillimolesPerLiter)
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
            time = (validUiModel.timeEditorModel as TimeEditorModel.Valid).instant,
            zoneOffset = validUiModel.timeEditorModel.zoneOffset,
            metadata = metadataMapper.toLibMetadata(validUiModel.metadataEditorModel),
            temperature = (validUiModel.temperatureEditorModel as TemperatureEditorModel.Valid).parsedValue.celsius,
            measurementLocation = (validUiModel.measurementLocation as SelectorEditorModel.Valid).value
        )

        is BasalMetabolicRateRecordEditorModel -> BasalMetabolicRateRecord(
            time = (validUiModel.timeEditorModel as TimeEditorModel.Valid).instant,
            zoneOffset = validUiModel.timeEditorModel.zoneOffset,
            metadata = metadataMapper.toLibMetadata(validUiModel.metadataEditorModel),
            basalMetabolicRate = (validUiModel.powerEditorModel as PowerEditorModel.Valid).parsedValue.kilocaloriesPerDay,
        )

        is BloodGlucoseLevelRecordEditorModel -> BloodGlucoseRecord(
            time = (validUiModel.timeEditorModel as TimeEditorModel.Valid).instant,
            zoneOffset = validUiModel.timeEditorModel.zoneOffset,
            metadata = metadataMapper.toLibMetadata(validUiModel.metadataEditorModel),
            level = BloodGlucose.millimolesPerLiter((validUiModel.level as BloodGlucoseLevelEditorModel.Valid).parsedValue),
//            specimenSource = TODO(),
//            mealType = TODO(),
//            relationToMeal = TODO()
        )
    }
}