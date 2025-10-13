package com.example.healthconnect.domain.entity

import android.os.RemoteException
import java.io.IOException

sealed class Result {

    data class PermissionRequired(
        val sdkPermission: String
    ) : Result()

    data class Success(
        val payload: Payload,
    ) : Result()

    data class IpcFailure(
        val exception: RemoteException
    ) : Result()

    data class UnpermittedAccess(
        val exception: SecurityException
    ) : Result()

    data class IoException(
        val exception: IOException
    ) : Result()

    data class UnhandledException(
        val exception: Exception
    ) : Result()
}
