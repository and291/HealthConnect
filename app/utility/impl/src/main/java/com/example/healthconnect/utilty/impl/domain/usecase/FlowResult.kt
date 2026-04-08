package com.example.healthconnect.utilty.impl.domain.usecase

import android.os.RemoteException
import java.io.IOException

sealed class FlowResult<out T> {

    class Data<T>(
        val item: T,
    ) : FlowResult<T>()

    /**
     * Terminal flow results.
     * After receiving a terminal result, the flow is canceled.
     */
    sealed class Terminal : FlowResult<Nothing>() {

        data class PermissionRequired(
            val sdkPermission: String,
        ) : Terminal()

        data class IpcFailure(
            val exception: RemoteException,
        ) : Terminal()

        data class UnpermittedAccess(
            val exception: SecurityException,
        ) : Terminal()

        data class IoException(
            val exception: IOException,
        ) : Terminal()

        data class UnhandledException(
            val exception: Exception,
        ) : Terminal()
    }
}