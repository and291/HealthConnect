package com.example.healthconnect.domain


interface LibraryRepository {

    fun getSdkStatus(): Int
    suspend fun getGrantedPermissions(): Set<String>
}
