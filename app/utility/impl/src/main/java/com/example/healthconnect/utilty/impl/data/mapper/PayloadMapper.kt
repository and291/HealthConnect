package com.example.healthconnect.utilty.impl.data.mapper

import com.example.healthconnect.utilty.api.domain.entity.Payload

class PayloadMapper {

    fun mapDeletedRecord(): Payload = Payload.Removed

    fun mapUpdateRecord(): Payload = Payload.Updated
}
