package com.example.healthconnect.components.impl.di

import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.components.impl.data.mapper.MeasurementLocationMapper
import com.example.healthconnect.components.impl.ui.ComponentProviderImpl
import com.example.healthconnect.components.impl.ui.metadata.ComponentViewModelFactory
import com.example.healthconnect.components.impl.ui.metadata.mapper.DeviceTypeMapper
import com.example.healthconnect.components.impl.ui.metadata.mapper.RecordingMethodMapper
import kotlin.getValue

object Di {

    val componentProvider: ComponentProvider by lazy {
        ComponentProviderImpl()
    }

    internal val componentViewModelFactory by lazy {
        ComponentViewModelFactory()
    }

    internal val deviceTypeMapper = DeviceTypeMapper()
    internal val recordingMethodMapper = RecordingMethodMapper()
    internal val measurementLocationMapper = MeasurementLocationMapper()
}