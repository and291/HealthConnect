package com.example.healthconnect.utilty.impl.domain

import android.os.RemoteException
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import java.io.IOException

class FlowResultMapperTest {

    private val mapper = FlowResultMapper()

    @Test
    fun remoteException_mapsToIpcFailure() {
        val exception = RemoteException("ipc error")

        val result = mapper.mapTerminalState(exception)

        assertEquals(FlowResult.Terminal.IpcFailure(exception), result)
    }

    @Test
    fun securityException_mapsToUnpermittedAccess() {
        val exception = SecurityException("permission denied")

        val result = mapper.mapTerminalState(exception)

        assertEquals(FlowResult.Terminal.UnpermittedAccess(exception), result)
    }

    @Test
    fun ioException_mapsToIoException() {
        val exception = IOException("disk error")

        val result = mapper.mapTerminalState(exception)

        assertEquals(FlowResult.Terminal.IoException(exception), result)
    }

    @Test
    fun unknownException_mapsToUnhandledException() {
        val exception = IllegalStateException("unexpected")

        val result = mapper.mapTerminalState(exception)

        assertEquals(FlowResult.Terminal.UnhandledException(exception), result)
    }

    @Test
    fun mappedTerminal_holdsOriginalExceptionReference() {
        val exception = RuntimeException("original")

        val result = mapper.mapTerminalState(exception) as FlowResult.Terminal.UnhandledException

        assertSame(exception, result.exception)
    }
}
