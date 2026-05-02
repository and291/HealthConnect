package com.example.healthconnect.ui

import androidx.lifecycle.ViewModel
import com.example.healthconnect.domain.LibraryRepository
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Test

class ParameterlessViewModelFactoryTest {

    private val stubRepository = object : LibraryRepository {
        override fun getSdkStatus() = 0
        override suspend fun getGrantedPermissions() = emptySet<String>()
    }

    private val factory = ParameterlessViewModelFactory(stubRepository)

    @Test
    fun create_returnsActivityViewModel() {
        val vm = factory.create(ActivityViewModel::class.java)
        assertNotNull(vm)
    }

    @Test
    fun create_throwsIllegalStateException_forUnknownViewModelClass() {
        assertThrows(IllegalStateException::class.java) {
            factory.create(ViewModel::class.java)
        }
    }
}
