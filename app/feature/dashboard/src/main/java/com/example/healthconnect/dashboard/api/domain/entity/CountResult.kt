package com.example.healthconnect.dashboard.api.domain.entity

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Outcome of counting records of a single type. Data-layer terminal failures are collapsed into
 * [Failed] together with an icon describing the failure; the mapping happens in the integration.
 */
sealed interface CountResult {

    data class Counted(val count: Int) : CountResult

    data class Failed(val errorIcon: ImageVector) : CountResult
}
