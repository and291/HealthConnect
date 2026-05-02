package com.example.healthconnect.utilty.api.ui.mapper

import androidx.annotation.StringRes
import com.example.healthconnect.models.api.domain.record.Model
import kotlin.reflect.KClass

interface RecordTypeNameMapper {
    @StringRes
    fun nameRes(type: KClass<out Model>): Int
}
