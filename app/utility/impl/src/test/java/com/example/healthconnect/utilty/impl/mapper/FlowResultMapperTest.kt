package com.example.healthconnect.utilty.impl.mapper

import android.os.RemoteException
import com.example.healthconnect.utilty.impl.data.mapper.FlowResultMapper
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import org.junit.Assert
import org.junit.Test
import java.io.IOException

class FlowResultMapperTest {

    private val mapper = FlowResultMapper()

    @Test
    fun remoteException_mapsToIpcFailure() {
        val exception = RemoteException("ipc error")

        val result = mapper.mapTerminal(exception, "")

        Assert.assertEquals(FlowResult.Terminal.IpcFailure(exception), result)
    }

    @Test
    fun securityException_mapsToUnpermittedAccess() {
        val exception = SecurityException("permission denied")

        val result = mapper.mapTerminal(exception, "")

        Assert.assertEquals(FlowResult.Terminal.UnpermittedAccess(exception, ""), result)
    }

    @Test
    fun securityException_withMissingPermission_propagatesMissingPermission() {
        val exception = SecurityException("permission denied")
        val permission = "android.permission.health.READ_STEPS"

        val result = mapper.mapTerminal(exception, permission) as FlowResult.Terminal.UnpermittedAccess

        Assert.assertEquals(permission, result.missingPermission)
    }

    @Test
    fun ioException_mapsToIoException() {
        val exception = IOException("disk error")

        val result = mapper.mapTerminal(exception, "")

        Assert.assertEquals(FlowResult.Terminal.IoException(exception), result)
    }

    @Test
    fun unknownException_mapsToUnhandledException() {
        val exception = IllegalStateException("unexpected")

        val result = mapper.mapTerminal(exception, "")

        Assert.assertEquals(FlowResult.Terminal.UnhandledException(exception), result)
    }

    @Test
    fun mappedTerminal_holdsOriginalExceptionReference() {
        val exception = RuntimeException("original")

        val result = mapper.mapTerminal(exception, "") as FlowResult.Terminal.UnhandledException

        Assert.assertSame(exception, result.throwable)
    }
}
