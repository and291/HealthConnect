package com.example.healthconnect.domain.model

import androidx.health.connect.client.records.Record

sealed class Payload {

    data class InsertList(
        val recordIdsList: List<String>
    ) : Payload()

    data class ReadList<T : Record>(
        val list: List<T>,
        val pageToken: String?
    ) : Payload()
}
