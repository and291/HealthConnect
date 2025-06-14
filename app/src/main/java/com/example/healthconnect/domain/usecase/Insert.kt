package com.example.healthconnect.domain.usecase

import android.os.RemoteException
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.Record
import com.example.healthconnect.domain.LibraryRepository
import com.example.healthconnect.domain.model.Result
import java.io.IOException

class Insert(
    private val libraryRepository: LibraryRepository,
) {

    suspend operator fun invoke(
        record: Record,
    ): Result {
        val requiredPermission = HealthPermission.getWritePermission(record::class)
        if (!libraryRepository.getGrantedPermissions().contains(requiredPermission)) {
            return Result.PermissionRequired(requiredPermission)
        }

        return try {
            val response = libraryRepository.insertRecords(listOf(record))
            Result.Success(response.toString())
        } catch (e: Exception) {
            when (e) {
                is RemoteException -> Result.IpcFailure(e)
                is SecurityException -> Result.UnpermittedAccess(e)
                is IOException -> Result.IoException(e)
                else -> Result.UnhandledException(e)
            }
        }
    }
}
