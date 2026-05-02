package com.example.healthconnect.utilty.impl.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import com.example.healthconnect.editor.api.domain.record.factory.ModelFactory
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.permissions.api.domain.framework.usecase.LibraryPermissionResolver
import com.example.healthconnect.permissions.api.domain.framework.usecase.PermissionController
import com.example.healthconnect.permissions.api.domain.framework.usecase.PermissionCoordinator
import com.example.healthconnect.utilty.api.domain.usecase.Insert
import com.example.healthconnect.utilty.api.domain.usecase.Update
import com.example.healthconnect.utilty.api.ui.mapper.RecordTypeNameMapper
import com.example.healthconnect.utilty.impl.data.LibraryPermissionResolverImpl
import com.example.healthconnect.utilty.impl.data.PermissionControllerImpl
import com.example.healthconnect.utilty.impl.data.mapper.FlowResultMapper
import com.example.healthconnect.utilty.impl.data.mapper.PayloadMapper
import com.example.healthconnect.utilty.impl.data.mapper.ReadParamsMapper
import com.example.healthconnect.utilty.impl.data.mapper.ResultMapper
import com.example.healthconnect.utilty.impl.data.mapper.TypeMapper
import com.example.healthconnect.utilty.impl.data.repository.LibraryRepositoryImpl
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.SupportedModels
import com.example.healthconnect.utilty.impl.domain.entity.Pager
import com.example.healthconnect.utilty.impl.domain.entity.ReadParams
import com.example.healthconnect.utilty.impl.domain.usecase.Count
import com.example.healthconnect.utilty.impl.domain.usecase.Delete
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import com.example.healthconnect.utilty.impl.domain.usecase.InsertImpl
import com.example.healthconnect.utilty.impl.domain.usecase.ReadAll
import com.example.healthconnect.utilty.impl.domain.usecase.UpdateImpl
import com.example.healthconnect.utilty.impl.ui.RecordsViewModelFactory
import com.example.healthconnect.utilty.impl.ui.mapper.FlowResultTerminalIconMapper
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeIconMapper
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeNameMapperImpl
import com.example.healthconnect.utilty.impl.ui.screen.dashboard.DashboardViewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

object Di { //TODO move to dagger. keep all features
    var isPreview = true

    lateinit var applicationContext: Context
    lateinit var modelFactory: ModelFactory
    lateinit var permissionCoordinator: PermissionCoordinator

    private val healthConnectClient: HealthConnectClient by lazy {
        HealthConnectClient.getOrCreate(applicationContext)
    }

    val permissionController: PermissionController by lazy {
        PermissionControllerImpl(healthConnectClient)
    }

    val permissionResolver: LibraryPermissionResolver by lazy {
        LibraryPermissionResolverImpl(typeMapper)
    }

    val recordTypeNameMapper: RecordTypeNameMapper by lazy { RecordTypeNameMapperImpl() }

    val allModelTypes: List<KClass<out Model>> = SupportedModels.all

    private val libraryRepository by lazy {
        if (isPreview) {
            object : LibraryRepository {
                override fun getSdkStatus(): Int {
                    return HealthConnectClient.SDK_AVAILABLE
                }

                override suspend fun getGrantedPermissions(): Set<String> {
                    return setOf("sdk:permission")
                }

                override suspend fun updateRecords(records: List<Model>) = error("No impl")

                override suspend fun insertRecords(records: List<Model>): List<String> = error("No impl")

                override suspend fun removeRecord(recordType: KClass<out Model>, metadataId: String) = error("No impl")

                override fun <M : Model> pager(params: ReadParams<M>): Pager = error("No impl")

                override fun <M : Model> count(params: ReadParams<M>): Flow<FlowResult<Int>> = error("No impl")
            }
        } else {
            LibraryRepositoryImpl(
                applicationContext = applicationContext,
                typeMapper = typeMapper,
                readParamsMapper = readParamsMapper,
                modelFactory = modelFactory,
                flowResultMapper = flowResultMapper,
            )
        }
    }

    private val payloadMapper = PayloadMapper()
    private val resultMapper = ResultMapper()
    private val typeMapper = TypeMapper()
    private val readParamsMapper = ReadParamsMapper(typeMapper)
    private val flowResultMapper = FlowResultMapper()

    val insert: Insert by lazy {
        InsertImpl(libraryRepository, resultMapper)
    }

    val update: Update by lazy {
        UpdateImpl(libraryRepository, resultMapper, payloadMapper)
    }

    private val delete by lazy {
        Delete(libraryRepository, resultMapper, payloadMapper)
    }

    private val readAll by lazy {
        ReadAll(libraryRepository)
    }

    val recordsViewModelFactory by lazy {
        RecordsViewModelFactory(
            readAll = readAll,
            delete = delete,
            coordinator = permissionCoordinator,
            recordTypeNameMapper = recordTypeNameMapper,
        )
    }

    private val recordTypeIconMapper by lazy { RecordTypeIconMapper() }
    private val flowResultTerminalIconMapper by lazy { FlowResultTerminalIconMapper() }

    private val count by lazy {
        Count(libraryRepository)
    }

    internal val dashboardViewModelFactory by lazy {
        DashboardViewModelFactory(
            count,
            recordTypeNameMapper,
            recordTypeIconMapper,
            flowResultTerminalIconMapper
        )
    }
}
