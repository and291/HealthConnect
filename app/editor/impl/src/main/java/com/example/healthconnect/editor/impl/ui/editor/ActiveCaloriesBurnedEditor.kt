package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.calories
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.components.api.ui.model.top.ValueComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.ActiveCaloriesBurnedModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class ActiveCaloriesBurnedEditor() : Editor<ActiveCaloriesBurnedRecord, ActiveCaloriesBurnedModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: ActiveCaloriesBurnedModel,
        event: ModelModificationEvent,
    ): ActiveCaloriesBurnedModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.Energy -> model.copy(
                energy = event.value
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: ActiveCaloriesBurnedRecord,
        mapper: MetadataMapper,
    ): ActiveCaloriesBurnedModel = ActiveCaloriesBurnedModel(
        time = TimeComponentModel.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        energy = ValueComponentModel.Dbl(
            parsedValue = record.energy.inCalories,
            type = ValueComponentModel.Type.Energy(),
        ),
    )

    override fun toRecord(
        validModel: ActiveCaloriesBurnedModel,
        mapper: MetadataMapper,
    ): ActiveCaloriesBurnedRecord = ActiveCaloriesBurnedRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        energy = (validModel.energy as ValueComponentModel.Dbl).parsedValue!!.calories,
    )

    override fun createDefault(): ActiveCaloriesBurnedRecord {
        val instant = Instant.now()
        return ActiveCaloriesBurnedRecord(
            startTime = instant.minusSeconds(60),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
            energy = 60.calories,
        )
    }
}