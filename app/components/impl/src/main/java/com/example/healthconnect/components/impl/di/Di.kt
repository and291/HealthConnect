package com.example.healthconnect.components.impl.di

import com.example.healthconnect.components.impl.data.mapper.MeasurementLocationMapper
import com.example.healthconnect.components.impl.ui.metadata.ComponentViewModelFactory
import com.example.healthconnect.components.impl.ui.metadata.mapper.DeviceTypeMapper
import com.example.healthconnect.components.impl.ui.metadata.mapper.RecordingMethodMapper
import kotlin.getValue

object Di {

    internal val componentViewModelFactory by lazy {
        ComponentViewModelFactory()
    }

    internal val deviceTypeMapper = DeviceTypeMapper()
    internal val recordingMethodMapper = RecordingMethodMapper()
    internal val measurementLocationMapper = MeasurementLocationMapper()

}