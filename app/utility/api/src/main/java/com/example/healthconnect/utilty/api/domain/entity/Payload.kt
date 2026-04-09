package com.example.healthconnect.utilty.api.domain.entity

//TODO тут как будто бы надо переделать. Сюда летят варианты ответов для всех типов запросов и вставки, и удадения и апдейта. Это странно
sealed class Payload {

    data class InsertList(
        val recordIdsList: List<String>
    ) : Payload()

    data object Removed : Payload()

    data object Updated : Payload()
}