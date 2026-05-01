package com.example.healthconnect.utilty.impl.data.repository

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.response.ReadRecordsResponse
import com.example.healthconnect.editor.api.domain.record.factory.ModelFactory
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.data.mapper.FlowResultMapper
import com.example.healthconnect.utilty.impl.domain.entity.Page
import com.example.healthconnect.utilty.impl.domain.entity.Pager
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion

/**
 * Demand-driven pager: emits one [Page] per [requestNextPage] call.
 * The first page is requested automatically when [startWithFirstPage] is true (default).
 *
 * [readPage] is called with the continuation token from the previous page, or null for the
 * first page. It must return a [PageData] containing the items and the next page token.
 * Returning a null token signals that there are no further pages and the flow completes.
 */
internal class PageIterator(
    private val readPage: suspend (pageToken: String?) -> ReadRecordsResponse<Record>,
    private val flowResultMapper: FlowResultMapper,
    private val modelFactory: ModelFactory,
    private val requiredPermission: String,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    startWithFirstPage: Boolean = true,
) : Pager {

    data class PageData(
        val items: List<Model>,
        val nextPageToken: String?,
    )

    private val pageRequests = Channel<Unit>(Channel.CONFLATED)

    init {
        if (startWithFirstPage) {
            pageRequests.trySend(Unit)
        }
    }

    override val pages: Flow<FlowResult<Page>> = flow<FlowResult<Page>> {
        var nextPageToken: String? = null
        do {
            pageRequests.receive()
            val page = readPage(nextPageToken).run {
                nextPageToken = pageToken
                Page(
                    items = records.map { modelFactory.create(it) },
                    hasNextPage = pageToken != null,
                )
            }
            emit(FlowResult.Data(page))
        } while (nextPageToken != null)
    }
        .onCompletion { pageRequests.close() }
        .catch { e -> emit(flowResultMapper.mapTerminal(e, requiredPermission)) }
        .flowOn(dispatcher)

    override fun requestNextPage() {
        pageRequests.trySend(Unit)
    }
}
