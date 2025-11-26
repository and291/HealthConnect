package com.example.healthconnect.components.api.ui.model

sealed class ComponentModel {

    abstract fun isValid(): Boolean
}