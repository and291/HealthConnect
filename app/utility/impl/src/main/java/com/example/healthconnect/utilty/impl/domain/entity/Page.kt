package com.example.healthconnect.utilty.impl.domain.entity

import com.example.healthconnect.models.api.domain.record.Model

data class Page<M : Model>(
    val items: List<M>,
)