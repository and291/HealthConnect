package com.example.healthconnect.permission_overview.di

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.example.healthconnect.permission_overview.api.domain.PermissionResolver
import com.example.healthconnect.permission_overview.api.domain.entity.IsGranted
import com.example.healthconnect.permission_overview.api.domain.entity.Permission
import com.example.healthconnect.permission_overview.api.domain.entity.PermissionEntry
import com.example.healthconnect.permission_overview.api.ui.PermissionContractProvider

internal sealed interface Dependencies {
    val applicationContext: Context
    val permissions: Set<PermissionEntry>
    val resolver: PermissionResolver
    val permissionContractProvider: PermissionContractProvider
    val isPreview: Boolean
}

internal class ProductionDependencies(
    override val applicationContext: Context,
    override val permissions: Set<PermissionEntry>,
    override val resolver: PermissionResolver,
    override val permissionContractProvider: PermissionContractProvider,
    override val isPreview: Boolean = false,
) : Dependencies

internal class PreviewDependencies(
    override val applicationContext: Context,
    override val permissions: Set<PermissionEntry> = emptySet(),
    override val resolver: PermissionResolver = object : PermissionResolver {
        override suspend fun isGranted(permission: Permission): Boolean = true

        override suspend fun isGranted(permissions: Set<Permission>): Set<Pair<Permission, IsGranted>> {
            return permissions.map { it to IsGranted(true) }.toSet()
        }

    },
    override val permissionContractProvider: PermissionContractProvider = object : PermissionContractProvider {
        override fun getLibraryContract(): ActivityResultContract<Set<String>, Set<String>> {
            return object : ActivityResultContract<Set<String>, Set<String>>() {
                override fun createIntent(
                    context: Context,
                    input: Set<String>,
                ): Intent {
                    return Intent()
                }

                override fun parseResult(
                    resultCode: Int,
                    intent: Intent?,
                ): Set<String> {
                    return emptySet()
                }

            }
        }

        override fun getLibrarySettingsAction(): String = ""

    },
    override val isPreview: Boolean = true,
) : Dependencies