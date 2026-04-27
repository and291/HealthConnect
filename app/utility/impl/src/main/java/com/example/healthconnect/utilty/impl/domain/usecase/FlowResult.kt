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
    sealed class Terminal(
        open val throwable: Throwable,
    ) : FlowResult<Nothing>() {

        data class IpcFailure(
            val exception: RemoteException,
        ) : Terminal(exception)

        data class UnpermittedAccess(
            val exception: SecurityException,
        ) : Terminal(exception)

        data class IoException(
            val exception: IOException,
        ) : Terminal(exception)

        data class UnhandledException(
            override val throwable: Throwable,
        ) : Terminal(throwable)
    }
}