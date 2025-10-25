package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Power
import androidx.health.connect.client.units.kilocaloriesPerDay
import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.BasalMetabolicRateModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class BasalMetabolicRateEditor() :
    Editor<BasalMetabolicRateRecord, BasalMetabolicRateModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: BasalMetabolicRateModel,
        event: ModelModificationEvent,
    ): BasalMetabolicRateModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueComponentModel.Type.Power -> model.copy(
                power = event.value
            )

            else -> throw NotImplementedError()
        }

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: BasalMetabolicRateRecord,
        mapper: MetadataMapper,
    ): BasalMetabolicRateModel = BasalMetabolicRateModel(
        time = TimeComponentModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        power = DoubleValueComponentModel.Valid(
            parsedValue = record.basalMetabolicRate.inKilocaloriesPerDay,
            type = DoubleValueComponentModel.Type.Power(),
        )
    )

    override fun toRecord(
        validModel: BasalMetabolicRateModel,
        mapper: MetadataMapper,
    ): BasalMetabolicRateRecord = BasalMetabolicRateRecord(
        time = (validModel.time as TimeComponentModel.Valid).instant,
        zoneOffset = (validModel.time as TimeComponentModel.Valid).zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        basalMetabolicRate = (validModel.power as DoubleValueComponentModel.Valid).parsedValue.kilocaloriesPerDay,
    )

    override fun createDefault(): BasalMetabolicRateRecord = BasalMetabolicRateRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        basalMetabolicRate = Power.Companion.kilocaloriesPerDay(2500.0),
    )
}