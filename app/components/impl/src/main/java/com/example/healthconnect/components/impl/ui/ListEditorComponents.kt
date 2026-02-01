package com.example.healthconnect.components.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthconnect.components.api.ui.model.ComponentModel
import com.example.healthconnect.components.api.ui.model.sub.ExerciseLapComponentModel
import com.example.healthconnect.components.api.ui.model.sub.ExerciseRouteComponentModel
import com.example.healthconnect.components.api.ui.model.sub.ExerciseSegmentComponentModel
import com.example.healthconnect.components.api.ui.model.top.ListComponentModel
import java.time.Instant

@Suppress("UNCHECKED_CAST")
fun <T : ComponentModel> LazyListScope.listEditorItems(
    model: ListComponentModel<T>,
    modifier: Modifier = Modifier,
    onChanged: (ListComponentModel<T>) -> Unit,
) {
    item(key = "list_header_${model.presentationId}") {
        val showAddButton = when (val type = model.type) {
            is ListComponentModel.Type.ExerciseRoute -> type.result != ListComponentModel.Type.ExerciseRoute.RouteResult.ConsentRequired
            else -> true
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = model.type.label,
                style = MaterialTheme.typography.titleMedium
            )
            if (showAddButton) {
                Button(onClick = {
                    val newItem = when (model.type) {
                        is ListComponentModel.Type.ExerciseSegments -> ExerciseSegmentComponentModel(
                            startTime = Instant.now(),
                            endTime = Instant.now().plusSeconds(60),
                            segmentType = 0,
                            repetitions = 0
                        )

                        is ListComponentModel.Type.ExerciseLaps -> ExerciseLapComponentModel(
                            startTime = Instant.now(),
                            endTime = Instant.now().plusSeconds(60),
                            lengthInMeters = null
                        )

                        is ListComponentModel.Type.ExerciseRoute -> ExerciseRouteComponentModel(
                            time = Instant.now(),
                            latitude = 0.0,
                            longitude = 0.0,
                            altitude = null,
                            horizontalAccuracy = null,
                            verticalAccuracy = null
                        )
                    }
                    onChanged(model.copy(items = model.items + (newItem as T)))
                }) {
                    Text("Add")
                }
            }
        }
    }

    if (model.type is ListComponentModel.Type.ExerciseRoute) {
        val routeType = model.type as ListComponentModel.Type.ExerciseRoute
        when (routeType.result) {
            ListComponentModel.Type.ExerciseRoute.RouteResult.ConsentRequired -> {
                item(key = "list_consent_required_${model.presentationId}") {
                    Text(
                        text = "Lack of permissions to access route data.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            ListComponentModel.Type.ExerciseRoute.RouteResult.NoData -> {
                if (model.items.isEmpty()) {
                    item(key = "list_no_data_${model.presentationId}") {
                        Text(
                            text = "No data yet. You can add some using the button above.",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            ListComponentModel.Type.ExerciseRoute.RouteResult.Data -> {
                // Normal items display
            }
        }
    }

    model.items.forEachIndexed { index, itemModel ->
        item(key = "list_item_${model.presentationId}_$index") {
            when (itemModel) {
                is ExerciseSegmentComponentModel -> ExerciseSegmentItem(
                    model = itemModel,
                    onChanged = { updatedItem ->
                        val newItems = model.items.toMutableList()
                        newItems[index] = updatedItem as T
                        onChanged(model.copy(items = newItems))
                    },
                    onDelete = {
                        val newItems = model.items.toMutableList()
                        newItems.removeAt(index)
                        onChanged(model.copy(items = newItems))
                    },
                    modifier = modifier
                )

                is ExerciseLapComponentModel -> ExerciseLapItem(
                    model = itemModel,
                    onChanged = { updatedItem ->
                        val newItems = model.items.toMutableList()
                        newItems[index] = updatedItem as T
                        onChanged(model.copy(items = newItems))
                    },
                    onDelete = {
                        val newItems = model.items.toMutableList()
                        newItems.removeAt(index)
                        onChanged(model.copy(items = newItems))
                    },
                    modifier = modifier
                )

                is ExerciseRouteComponentModel -> ExerciseRouteLocationItem(
                    location = itemModel,
                    onChanged = { updatedItem ->
                        val newItems = model.items.toMutableList()
                        newItems[index] = updatedItem as T
                        onChanged(model.copy(items = newItems))
                    },
                    onDelete = {
                        val newItems = model.items.toMutableList()
                        newItems.removeAt(index)
                        onChanged(model.copy(items = newItems))
                    },
                    modifier = modifier
                )
            }
        }
    }
    item(key = "list_divider_${model.presentationId}") {
        HorizontalDivider(modifier = modifier.padding(vertical = 8.dp))
    }
}

@Preview(showBackground = true, name = "Exercise Segments")
@Composable
private fun ExerciseSegmentsPreview() {
    val model = ListComponentModel(
        items = listOf(
            ExerciseSegmentComponentModel(
                startTime = Instant.now(),
                endTime = Instant.now().plusSeconds(60),
                segmentType = 1,
                repetitions = 5
            )
        ),
        type = ListComponentModel.Type.ExerciseSegments
    )
    ListEditorPreviewWrapper(model)
}

@Preview(showBackground = true, name = "Exercise Laps")
@Composable
private fun ExerciseLapsPreview() {
    val model = ListComponentModel(
        items = listOf(
            ExerciseLapComponentModel(
                startTime = Instant.now(),
                endTime = Instant.now().plusSeconds(600),
                lengthInMeters = 400.0
            )
        ),
        type = ListComponentModel.Type.ExerciseLaps
    )
    ListEditorPreviewWrapper(model)
}

@Preview(showBackground = true, name = "Exercise Route - Data")
@Composable
private fun ExerciseRouteDataPreview() {
    val model = ListComponentModel(
        items = listOf(
            ExerciseRouteComponentModel(
                time = Instant.now(),
                latitude = 37.7749,
                longitude = -122.4194,
                altitude = 10.0,
                horizontalAccuracy = 5.0,
                verticalAccuracy = 2.0
            )
        ),
        type = ListComponentModel.Type.ExerciseRoute(ListComponentModel.Type.ExerciseRoute.RouteResult.Data)
    )
    ListEditorPreviewWrapper(model)
}

@Preview(showBackground = true, name = "Exercise Route - Consent Required")
@Composable
private fun ExerciseRouteConsentRequiredPreview() {
    val model = ListComponentModel<ExerciseRouteComponentModel>(
        items = emptyList(),
        type = ListComponentModel.Type.ExerciseRoute(ListComponentModel.Type.ExerciseRoute.RouteResult.ConsentRequired)
    )
    ListEditorPreviewWrapper(model)
}

@Preview(showBackground = true, name = "Exercise Route - No Data")
@Composable
private fun ExerciseRouteNoDataPreview() {
    val model = ListComponentModel<ExerciseRouteComponentModel>(
        items = emptyList(),
        type = ListComponentModel.Type.ExerciseRoute(ListComponentModel.Type.ExerciseRoute.RouteResult.NoData)
    )
    ListEditorPreviewWrapper(model)
}

@Composable
private fun <T : ComponentModel> ListEditorPreviewWrapper(model: ListComponentModel<T>) {
    MaterialTheme {
        Surface {
            LazyColumn {
                listEditorItems(
                    model = model,
                    onChanged = {}
                )
            }
        }
    }
}
