package com.example.healthconnect.record_list.api.domain.entity

/**
 * Outcome of loading a single page of records.
 *
 * Terminal failures from the data layer are collapsed into two cases here: a missing permission
 * ([PermissionDenied]) and any other failure ([Error]). The mapping from the data layer's richer
 * result type happens in the app-module integration.
 */
sealed interface RecordPage {

    data class Records(
        val records: List<RecordModel>,
        val hasNextPage: Boolean,
    ) : RecordPage

    data object PermissionDenied : RecordPage

    data class Error(
        val message: String,
    ) : RecordPage
}
