package com.example.healthconnect.utilty.impl.ui.screen.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material3.Badge
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthconnect.models.api.domain.record.Steps
import com.example.healthconnect.utilty.impl.R
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import com.example.healthconnect.utilty.impl.ui.screen.dashboard.model.DashboardItem

@Composable
fun DashboardTile(
    item: DashboardItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.height(96.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Badge(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ) {
                    Text(text = item.state.toString())
                }
            }
            Text(
                text = stringResource(item.nameRes),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardTileWithCountPreview() {
    DashboardTile(
        item = DashboardItem(
            recordType = Steps::class,
            nameRes = R.string.record_type_steps,
            icon = Icons.AutoMirrored.Filled.DirectionsWalk,
            state = DashboardItem.LoadingState.InProgress,
        ),
        onClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun DashboardTileNoCountPreview() {
    DashboardTile(
        item = DashboardItem(
            recordType = Steps::class,
            nameRes = R.string.record_type_steps,
            icon = Icons.AutoMirrored.Filled.DirectionsWalk,
            state = DashboardItem.LoadingState.Loaded(FlowResult.Data(42))
        ),
        onClick = {},
    )
}
