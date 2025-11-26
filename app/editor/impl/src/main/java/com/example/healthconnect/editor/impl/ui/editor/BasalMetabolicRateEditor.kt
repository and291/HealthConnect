package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Power
import androidx.health.connect.client.units.kilocaloriesPerDay
import com.example.healthconnect.components.api.ui.model.ValueComponentModel
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

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.Power -> model.copy(
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
        time = TimeComponentModel.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        power = ValueComponentModel.Dbl(
            parsedValue = record.basalMetabolicRate.inKilocaloriesPerDay,
            type = ValueComponentModel.Type.Power(),
        )
    )

    override fun toRecord(
        validModel: BasalMetabolicRateModel,
        mapper: MetadataMapper,
    ): BasalMetabolicRateRecord = BasalMetabolicRateRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        basalMetabolicRate = (validModel.power as ValueComponentModel.Dbl).parsedValue!!.kilocaloriesPerDay,
    )

    override fun createDefault(): BasalMetabolicRateRecord = BasalMetabolicRateRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        basalMetabolicRate = Power.Companion.kilocaloriesPerDay(2500.0),
    )
}