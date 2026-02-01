package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.Vo2MaxRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.ui.model.top.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.top.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.Vo2MaxModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class Vo2MaxEditor() : Editor<Vo2MaxRecord, Vo2MaxModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: Vo2MaxModel,
        event: ModelModificationEvent,
    ): Vo2MaxModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueSelected -> when (event.selector.type) {

            is SelectorComponentModel.Type.Vo2MaxMeasurementMethod -> model.copy(
                measurementMethod = SelectorComponentModel(
                    value = event.selector.value,
                    type = event.selector.type,
                )
            )

            else -> throw NotImplementedError()
        }

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.Vo2Max -> model.copy(
                vo2MillilitersPerMinuteKilogram = event.value
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: Vo2MaxRecord,
        mapper: MetadataMapper,
    ): Vo2MaxModel = Vo2MaxModel(
        time = TimeComponentModel.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        vo2MillilitersPerMinuteKilogram = ValueComponentModel.Dbl(
            parsedValue = record.vo2MillilitersPerMinuteKilogram,
            type = ValueComponentModel.Type.Vo2Max(),
        ),
        measurementMethod = SelectorComponentModel(
            value = record.measurementMethod,
            type = SelectorComponentModel.Type.Vo2MaxMeasurementMethod(),
        ),
    )

    override fun toRecord(
        validModel: Vo2MaxModel,
        mapper: MetadataMapper,
    ): Vo2MaxRecord = Vo2MaxRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        vo2MillilitersPerMinuteKilogram = (validModel.vo2MillilitersPerMinuteKilogram as ValueComponentModel.Dbl).parsedValue!!,
        measurementMethod = validModel.measurementMethod.value,
    )

    override fun createDefault(): Vo2MaxRecord = Vo2MaxRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        vo2MillilitersPerMinuteKilogram = 50.0,
    )
}