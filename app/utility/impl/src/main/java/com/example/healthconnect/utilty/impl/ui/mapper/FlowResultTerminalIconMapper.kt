package com.example.healthconnect.utilty.impl.ui.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SyncProblem
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult.Terminal

class FlowResultTerminalIconMapper {
    fun icon(terminal: Terminal): ImageVector = when (terminal) {
        is Terminal.UnpermittedAccess -> Icons.Default.Lock
        is Terminal.IpcFailure -> Icons.Default.SyncProblem
        is Terminal.IoException -> Icons.Default.CloudOff
        is Terminal.UnhandledException -> Icons.Default.Warning
    }
}