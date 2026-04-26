package com.example.healthconnect.utilty.impl.ui.screen.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.domain.entity.Pager
import com.example.healthconnect.utilty.impl.domain.entity.Page
import com.example.healthconnect.utilty.impl.domain.usecase.Delete
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import com.example.healthconnect.utilty.impl.domain.usecase.ReadAll
import com.example.healthconnect.utilty.impl.ui.screen.records.RecordsViewModel.Effect.OpenRecordScreen
import com.example.healthconnect.utilty.impl.ui.screen.records.RecordsViewModel.State.DisplayPage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class RecordsViewModel(
    private val recordType: KClass<out Model>,
    private val readAll: ReadAll,
    private val delete: Delete,
) : ViewModel() {

    private val _effect = Channel<Effect>(Channel.UNLIMITED)
    val effect: Flow<Effect> = _effect.receiveAsFlow()

    private val refreshChannel = Channel<Unit>(Channel.CONFLATED)
    private var currentPager: Pager? = null

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
        val pager = readAll(recordType)
        currentPager = pager

        return pager.pages
            .runningFold(State(emptyList(), isRefreshing = true, hasMorePages =  false)) { previousState, page ->
                val displayPage = when (page) {
                    is FlowResult.Data<Page> -> DisplayPage.Record(page.item.items)
                    is FlowResult.Terminal -> DisplayPage.Error(page.toString())
                }
                State(
                    pages = previousState.pages + displayPage,
                    hasMorePages = (page as? FlowResult.Data<Page>)?.item?.hasNextPage ?: false,
                    isRefreshing = false,
                )
            }
    }

    fun onEvent(event: Event) {
        when (event) {

            is Event.DeleteRecord -> viewModelScope.launch {
                delete(recordType = event.recordType, metadataId = event.metadataId)
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
                val records: List<Model>,
            ) : DisplayPage()

            data class Error(
                val message: String,
            ) : DisplayPage()
        }
    }

    sealed class Effect {

        data class OpenRecordScreen(
            val record: Model,
        ) : Effect()

        data class RequestSinglePermission(
            val sdkPermission: String,
        ) : Effect()
    }

    sealed class Event {

        data class OnRecordClick(
            val record: Model,
        ) : Event()

        data class DeleteRecord(
            val recordType: KClass<out Model>,
            val metadataId: String,
        ) : Event()

        data object Refresh : Event()

        data object NextPage : Event()
    }

    companion object {
        val RECORD_TYPE_KEY: CreationExtras.Key<KClass<out Model>> = CreationExtras.Key()
    }
}