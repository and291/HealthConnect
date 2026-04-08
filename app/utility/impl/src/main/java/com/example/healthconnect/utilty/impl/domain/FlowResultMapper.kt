package com.example.healthconnect.utilty.impl.domain

import android.os.RemoteException
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import java.io.IOException

class FlowResultMapper {

    fun mapTerminalState(e: Exception): FlowResult.Terminal = when (e) {
        //lib contract exceptions
        is RemoteException -> FlowResult.Terminal.IpcFailure(e)
        is SecurityException -> FlowResult.Terminal.UnpermittedAccess(e)
        is IOException -> FlowResult.Terminal.IoException(e)
        //default exception
        else -> FlowResult.Terminal.UnhandledException(e)
    }
}