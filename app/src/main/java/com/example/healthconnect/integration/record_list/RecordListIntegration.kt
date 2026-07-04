package com.example.healthconnect.integration.record_list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.record_list.api.domain.entity.RecordModel
import com.example.healthconnect.record_list.api.domain.entity.RecordPage
import com.example.healthconnect.record_list.api.domain.entity.RecordPager
import com.example.healthconnect.record_list.api.domain.usecase.DeleteRecord
import com.example.healthconnect.record_list.api.domain.usecase.LoadRecords
import com.example.healthconnect.record_list.api.ui.RecordSummaryFactory
import com.example.healthconnect.utilty.api.record.Model
import com.example.healthconnect.utilty.impl.domain.entity.Page
import com.example.healthconnect.utilty.impl.domain.entity.Pager
import com.example.healthconnect.utilty.impl.domain.usecase.Delete
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import com.example.healthconnect.utilty.impl.domain.usecase.ReadAll
import com.example.healthconnect.utilty.impl.ui.screen.records.summary.Summary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.KClass

/** Wraps a utility [Model] as the record-list module's opaque [RecordModel] handle. */
class RecordModelAdapter(val model: Model) : RecordModel {
    override fun metadataId(): String = model.metadata.id.value
}

fun unwrap(record: RecordModel): Model = (record as RecordModelAdapter).model

/** Adapts the utility paging [Pager] (and its richer [FlowResult]) to the module's [RecordPager]. */
class RecordPagerAdapter(private val pager: Pager) : RecordPager {

    override val pages: Flow<RecordPage> = pager.pages.map { it.toRecordPage() }

    override fun requestNextPage() = pager.requestNextPage()

    private fun FlowResult<Page>.toRecordPage(): RecordPage = when (this) {
        is FlowResult.Data<Page> -> RecordPage.Records(
            records = item.items.map(::RecordModelAdapter),
            hasNextPage = item.hasNextPage,
        )

        is FlowResult.Terminal.UnpermittedAccess -> RecordPage.PermissionDenied

        is FlowResult.Terminal -> RecordPage.Error(toString())
    }
}

class LoadRecordsImpl(private val readAll: ReadAll) : LoadRecords {
    override fun invoke(recordType: KClass<*>): RecordPager {
        @Suppress("UNCHECKED_CAST")
        return RecordPagerAdapter(readAll(recordType as KClass<out Model>))
    }
}

class DeleteRecordImpl(private val delete: Delete) : DeleteRecord {
    override suspend fun invoke(recordType: KClass<*>, metadataId: String) {
        @Suppress("UNCHECKED_CAST")
        delete(recordType = recordType as KClass<out Model>, metadataId = metadataId)
    }
}

class RecordSummaryFactoryImpl : RecordSummaryFactory {
    @Composable
    override fun Summary(record: RecordModel, modifier: Modifier) {
        unwrap(record).Summary(modifier)
    }
}
