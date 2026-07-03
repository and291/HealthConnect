package com.example.healthconnect.utilty.api.data

interface PermissionRepository {

    fun libraryPermissions(): Set<LibraryPermission>
}