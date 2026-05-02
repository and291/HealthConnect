package com.example.healthconnect.utilty.impl.data.mapper

import android.os.RemoteException
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import java.io.IOException

class FlowResultMapper {

    fun mapTerminal(e: Throwable, requiredPermission: String): FlowResult.Terminal = when (e) {
        //lib contract exceptions
        is RemoteException -> FlowResult.Terminal.IpcFailure(e)
        is SecurityException -> FlowResult.Terminal.UnpermittedAccess(e, requiredPermission)
        is IOException -> FlowResult.Terminal.IoException(e)
        //default exception
        else -> FlowResult.Terminal.UnhandledException(e)
    }
}