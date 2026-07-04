package com.example.healthconnect.record_list.ui.screen.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthconnect.record_list.api.domain.entity.RecordModel
import com.example.healthconnect.record_list.api.domain.entity.RecordPage
import com.example.healthconnect.record_list.api.domain.entity.RecordPager
import com.example.healthconnect.record_list.api.domain.usecase.DeleteRecord
import com.example.healthconnect.record_list.api.domain.usecase.LoadRecords
import com.example.healthconnect.record_list.ui.screen.records.RecordsViewModel.Effect.OpenRecordScreen
import com.example.healthconnect.record_list.ui.screen.records.RecordsViewModel.State.DisplayPage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class RecordsViewModel(
    private val modelType: KClass<*>,
    private val loadRecords: LoadRecords,
    private val deleteRecord: DeleteRecord,
) : ViewModel() {

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect: Flow<Effect> = _effect.receiveAsFlow()

    private val refreshChannel = Channel<Unit>(Channel.CONFLATED)
    private var currentPager: RecordPager? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val stateFlow: StateFlow<State> = refreshChannel.receiveAsFlow()
        .flatMapLatest { pagedFlow() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = State(emptyList(), isRefreshing = true, hasMorePages = false),
        )

    init {
        onEvent(Event.Refresh)
    }

    private fun pagedFlow(): Flow<State> {
        val pager = loadRecords(modelType)
        currentPager = pager

        return pager.pages
            .runningFold(State(emptyList(), isRefreshing = true, hasMorePages = false)) { previousState, page ->
                val displayPage = when (page) {
                    is RecordPage.Records -> DisplayPage.Record(page.records)

                    is RecordPage.PermissionDenied -> {
                        requestPermissionAndRefreshOnGrant()
                        DisplayPage.PermissionDenied
                    }

                    is RecordPage.Error -> DisplayPage.Error(page.message)
                }
                State(
                    pages = previousState.pages + displayPage,
                    hasMorePages = (page as? RecordPage.Records)?.hasNextPage ?: false,
                    isRefreshing = false,
                )
            }
    }

    private fun requestPermissionAndRefreshOnGrant() {
        TODO("Not yet implemented")
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.DeleteRecord -> viewModelScope.launch {
                deleteRecord(recordType = event.recordType, metadataId = event.metadataId)
                onEvent(Event.Refresh)
            }

            is Event.OnRecordClick -> _effect.trySend(OpenRecordScreen(event.record))

            Event.Refresh -> refreshChannel.trySend(Unit)

            Event.NextPage -> currentPager?.requestNextPage()
        }
    }

    data class State(
        val pages: List<DisplayPage>,
        val hasMorePages: Boolean,
        val isRefreshing: Boolean,
    ) {
        sealed class DisplayPage {

            data class Record(
                val records: List<RecordModel>,
            ) : DisplayPage()

            data class Error(
                val message: String,
            ) : DisplayPage()

            data object PermissionDenied : DisplayPage()
        }
    }

    sealed class Effect {
        data class OpenRecordScreen(
            val record: RecordModel,
        ) : Effect()
    }

    sealed class Event {
        data class OnRecordClick(val record: RecordModel) : Event()
        data class DeleteRecord(
            val recordType: KClass<*>,
            val metadataId: String,
        ) : Event()
        data object Refresh : Event()
        data object NextPage : Event()
    }
}
