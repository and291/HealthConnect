package com.example.healthconnect.utilty.impl.data

import com.example.healthconnect.utilty.api.record.Model
import com.example.healthconnect.utilty.api.data.LibraryPermission
import com.example.healthconnect.utilty.api.data.PermissionRepository
import com.example.healthconnect.utilty.api.ui.mapper.RecordTypeNameMapper
import com.example.healthconnect.utilty.impl.data.mapper.TypeMapper
import kotlin.reflect.KClass

internal class PermissionRepositoryImpl(
    private val allModels: List<KClass<out Model>>,
    private val source: LibraryPermissionDataSource,
    private val nameMapper: RecordTypeNameMapper,
    private val typeMapper: TypeMapper,
): PermissionRepository {

    override fun libraryPermissions(): Set<LibraryPermission> = allModels.map { modelClass ->
        val recordKlass = typeMapper.toRecord(modelClass)
        LibraryPermission(
            nameResId = nameMapper.nameRes(modelClass),
            read = source.getReadPermission(recordKlass),
            write = source.getWritePermission(recordKlass),
        )
    }.toSet()
}
