package com.example.healthconnect.record_list.api.domain.entity

import kotlinx.coroutines.flow.Flow

/**
 * Cold, paginated stream of records. Collecting [pages] starts loading the first page;
 * [requestNextPage] asks the source to emit the next one.
 */
interface RecordPager {

    val pages: Flow<RecordPage>

    fun requestNextPage()
}
