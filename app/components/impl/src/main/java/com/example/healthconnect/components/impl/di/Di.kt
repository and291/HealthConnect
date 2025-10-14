package com.example.healthconnect.components.impl.di

import com.example.healthconnect.components.api.data.mapper.MeasurementLocationMapper
import com.example.healthconnect.components.impl.ui.metadata.ComponentViewModelFactory
import com.example.healthconnect.components.impl.ui.metadata.mapper.DeviceTypeMapper
import com.example.healthconnect.components.impl.ui.metadata.mapper.RecordingMethodMapper
import kotlin.getValue

object Di {

    val componentViewModelFactory by lazy {
        ComponentViewModelFactory()
    }

    val deviceTypeMapper = DeviceTypeMapper()
    val recordingMethodMapper = RecordingMethodMapper()
    val measurementLocationMapper = MeasurementLocationMapper()

}