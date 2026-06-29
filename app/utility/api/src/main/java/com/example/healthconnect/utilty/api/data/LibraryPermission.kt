package com.example.healthconnect.utilty.api.data

import androidx.annotation.StringRes

data class LibraryPermission(
    @get:StringRes
    @param:StringRes
    val nameResId: Int,
    val read: String,
    val write: String,
)