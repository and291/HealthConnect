package com.example.healthconnect.record_list.api.domain.entity

/**
 * Opaque handle to a single record rendered by the record list.
 *
 * The feature module never inspects the underlying record type — it only needs a stable
 * identity for deletion and passes the handle back untouched on navigation. The app-module
 * integration layer supplies the concrete implementation (see the `integration/record_list`
 * package) and unwraps it when needed.
 */
interface RecordModel {

    fun metadataId(): String
}
