package com.example.healthconnect.utilty.impl.domain.entity

import androidx.health.connect.client.records.Record

//TODO тут как будто бы надо переделать. Сюда летят варианты ответов для всех типов запросов и вставки, и удадения и апдейта. Это странно
sealed class Payload {

    data class InsertList(
        val recordIdsList: List<String>
    ) : Payload()

    data class ReadList<T : Record>(
        val list: List<T>,
        val pageToken: String?
    ) : Payload()

    data object Removed : Payload()

    data object Updated : Payload()
}
