package com.nuvio.app.features.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nuvio.app.core.ui.nuvio
import com.nuvio.app.features.watchprogress.WatchProgressRepository

/**
 * Watch statistics for the active profile, derived from the progress store.
 * Deliberately local-only: nothing here talks to a tracker.
 */
internal fun LazyListScope.watchStatsContent(isTablet: Boolean) {
    item {
        val progressState by WatchProgressRepository.uiState.collectAsStateWithLifecycle()
        val entries = progressState.entries

        val titles = entries.map { it.parentMetaId }.distinct().size
        val inProgress = entries.count { !it.isCompleted && it.lastPositionMs > 0L }
        val completed = entries.count { it.isCompleted }
        val movies = entries.map { it.parentMetaId }.distinct()
            .count { id -> entries.any { it.parentMetaId == id && it.parentMetaType == "movie" } }
        val series = titles - movies
        val watchedMs = entries.sumOf { it.lastPositionMs.coerceAtLeast(0L) }
        val hours = watchedMs / 3_600_000.0

        SettingsSection(title = "Overview", isTablet = isTablet) {
            SettingsGroup(isTablet = isTablet) {
                StatRow(label = "Titles watched", value = titles.toString(), isTablet = isTablet)
                SettingsGroupDivider(isTablet = isTablet)
                StatRow(label = "Movies", value = movies.toString(), isTablet = isTablet)
                SettingsGroupDivider(isTablet = isTablet)
                StatRow(label = "Series", value = series.toString(), isTablet = isTablet)
            }
        }
    }

    item {
        SettingsSection(title = "Activity", isTablet = isTablet) {
            SettingsGroup(isTablet = isTablet) {
                StatRow(label = "In progress", value = entries.count { !it.isCompleted && it.lastPositionMs > 0L }.toString(), isTablet = isTablet)
                SettingsGroupDivider(isTablet = isTablet)
                StatRow(label = "Completed", value = completed.toString(), isTablet = isTablet)
                SettingsGroupDivider(isTablet = isTablet)
                StatRow(label = "Total watch time", value = formatHours(hours), isTablet = isTablet)
            }
        }
    }

    if (entries.isEmpty()) {
        item {
            SettingsSection(title = "Nothing yet", isTablet = isTablet) {
                SettingsGroup(isTablet = isTablet) {
                    StatRow(
                        label = "Play something and your statistics will appear here.",
                        value = "",
                        isTablet = isTablet,
                    )
                }
            }
        }
    }
}

private fun formatHours(hours: Double): String {
    if (hours < 1.0) return "${(hours * 60).toInt()} min"
    val whole = hours.toInt()
    val minutes = ((hours - whole) * 60).toInt()
    return if (minutes == 0) "${whole}h" else "${whole}h ${minutes}m"
}

@Composable
private fun StatRow(label: String, value: String, isTablet: Boolean) {
    val tokens = MaterialTheme.nuvio
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = if (isTablet) 18.dp else 14.dp,
                vertical = if (isTablet) 14.dp else 12.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = tokens.colors.textPrimary,
        )
        if (value.isNotBlank()) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = tokens.colors.textSecondary,
            )
        }
    }
}
