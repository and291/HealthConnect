package com.example.healthconnect.editor.api.ui

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import com.example.healthconnect.editor.api.domain.entity.EditEvent
import com.example.healthconnect.editor.api.domain.entity.EditableField

interface ComponentFactory {
    fun LazyListScope.create(
        components: List<EditableField>,
        modifier: Modifier = Modifier.Companion,
        eventHandler: (EditEvent) -> Unit,
    )
}

