package com.example.healthconnect.editor.api.domain.entity

sealed class Result {

    data object Ok : Result()
    data object Error : Result()
}
