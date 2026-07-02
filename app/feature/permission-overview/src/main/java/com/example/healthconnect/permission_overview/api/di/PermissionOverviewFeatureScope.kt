package com.example.healthconnect.permission_overview.api.di

import android.app.Application
import com.example.healthconnect.permission_overview.api.domain.PermissionResolver
import com.example.healthconnect.permission_overview.api.domain.entity.PermissionEntry
import com.example.healthconnect.permission_overview.api.ui.PermissionContractProvider
import com.example.healthconnect.permission_overview.di.Locator
import com.example.healthconnect.permission_overview.di.ProductionDependencies
import java.io.Closeable

class PermissionOverviewFeatureScope(
    private val application: Application,
    private val permissions: Set<PermissionEntry>,
    private val resolver: PermissionResolver,
    private val permissionContractProvider: PermissionContractProvider,
    private val isPreview: Boolean = false,
) : Closeable {

    fun init() {
        Locator.impl = ProductionDependencies(
            applicationContext = application.applicationContext,
            permissions = permissions,
            resolver = resolver,
            permissionContractProvider = permissionContractProvider,
            isPreview = isPreview
        )
    }

    override fun close() {
        Locator.clear()
    }
}