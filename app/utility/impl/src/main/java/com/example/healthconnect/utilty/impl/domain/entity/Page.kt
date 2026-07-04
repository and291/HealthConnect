package com.example.healthconnect.utilty.impl.domain.entity

import com.example.healthconnect.utilty.api.domain.record.Model

data class Page(
    val items: List<Model>,
    val hasNextPage: Boolean,
)