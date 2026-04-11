package com.example.healthconnect.utilty.impl.data.mapper

import android.os.RemoteException
import com.example.healthconnect.utilty.api.domain.entity.Result
import java.io.IOException

class ResultMapper {

    fun mapException(e: Exception): Result = when (e) {
        //lib contract exceptions
        is RemoteException -> Result.IpcFailure(e)
        is SecurityException -> Result.UnpermittedAccess(e)
        is IOException -> Result.IoException(e)
        //default exception
        else -> Result.UnhandledException(e)
    }
}
