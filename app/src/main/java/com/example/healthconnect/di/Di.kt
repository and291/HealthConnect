package com.example.healthconnect.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.response.InsertRecordsResponse
import com.example.healthconnect.data.repository.LibraryRepositoryImpl
import com.example.healthconnect.domain.LibraryRepository
import com.example.healthconnect.domain.usecase.Insert
import com.example.healthconnect.ui.ParameterlessViewModelFactory

object Di { //move to dagger. keep all features
    var isPreview = true

    lateinit var applicationContext: Context

    private val libraryRepository by lazy {
        if (isPreview) {
            object : LibraryRepository {
                override fun getSdkStatus(): Int {
                    return HealthConnectClient.SDK_AVAILABLE
                }

                override suspend fun getGrantedPermissions(): Set<String> {
                    return setOf("sdk:permission")
                }

                override suspend fun insertRecords(records: List<Record>): InsertRecordsResponse {
                    TODO("Not yet implemented")
                }
            }
        } else {
            LibraryRepositoryImpl(applicationContext)
        }
    }

    val insert by lazy {
        Insert(libraryRepository)
    }

    val parameterlessViewModelFactory by lazy {
        ParameterlessViewModelFactory(applicationContext, libraryRepository, insert)
    }

}
