package com.example.healthconnect.components.api.ui.model

sealed class SelectorEditorModel() : ComponentEditorModel() {
    abstract val value: Int
    abstract val selectorType: SelectorType

    data class Valid(
        override val value: Int,
        override val selectorType: SelectorType,
    ) : SelectorEditorModel()

    data class Invalid(
        override val value: Int,
        override val selectorType: SelectorType,
    ) : SelectorEditorModel()
}