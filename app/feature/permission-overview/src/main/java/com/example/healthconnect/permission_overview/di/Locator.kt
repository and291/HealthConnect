package com.example.healthconnect.permission_overview.di

internal object Locator {

    private var _impl: Dependencies? = null

    internal var impl: Dependencies
        get() = requireNotNull(_impl) {
            "Dependencies in ${this::class.qualifiedName} are not initialized"
        }
        set(value) {
            _impl = value
        }

    internal fun clear() {
        _impl = null
    }
}
