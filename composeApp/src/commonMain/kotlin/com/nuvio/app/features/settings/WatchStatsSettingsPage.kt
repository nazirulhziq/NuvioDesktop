package com.nuvio.app.features.settings

import androidx.compose.foundation.layout.Arrangement
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
import com.nuvio.app.features.watchprogress.WatchProgressEntry
import com.nuvio.app.features.watchprogress.WatchProgressRepository

/** Aggregated numbers for the active profile. Local only — nothing is synced. */
private data class WatchStats(
    val titles: Int,
    val movies: Int,
    val series: Int,
    val inProgress: Int,
    val completed: Int,
    val watchedHours: Double,
    val isEmpty: Boolean,
)

private fun statsOf(entries: List<WatchProgressEntry>): WatchStats {
    val parentIds = entries.map { it.parentMetaId }.distinct()
    val movieIds = parentIds.filter { id ->
        entries.any { it.parentMetaId == id && it.parentMetaType == "movie" }
    }
    return WatchStats(
        titles = parentIds.size,
        movies = movieIds.size,
        series = parentIds.size - movieIds.size,
        inProgress = entries.count { !it.isCompleted && it.lastPositionMs > 0L },
        completed = entries.count { it.isCompleted },
        watchedHours = entries.sumOf { it.lastPositionMs.coerceAtLeast(0L) } / 3_600_000.0,
        isEmpty = entries.isEmpty(),
    )
}

private fun formatHours(hours: Double): String {
    if (hours < 1.0) return "${(hours * 60).toInt()} min"
    val whole = hours.toInt()
    val minutes = ((hours - whole) * 60).toInt()
    return if (minutes == 0) "${whole}h" else "${whole}h ${minutes}m"
}

internal fun LazyListScope.watchStatsContent(isTablet: Boolean) {
    item {
        val progressState by WatchProgressRepository.uiState.collectAsStateWithLifecycle()
        val stats = statsOf(progressState.entries)

        SettingsSection(title = "Overview", isTablet = isTablet) {
            SettingsGroup(isTablet = isTablet) {
                StatRow("Titles watched", stats.titles.toString(), isTablet)
                SettingsGroupDivider(isTablet = isTablet)
                StatRow("Movies", stats.movies.toString(), isTablet)
                SettingsGroupDivider(isTablet = isTablet)
                StatRow("Series", stats.series.toString(), isTablet)
            }
        }
    }

    item {
        val progressState by WatchProgressRepository.uiState.collectAsStateWithLifecycle()
        val stats = statsOf(progressState.entries)

        SettingsSection(title = "Activity", isTablet = isTablet) {
            SettingsGroup(isTablet = isTablet) {
                StatRow("In progress", stats.inProgress.toString(), isTablet)
                SettingsGroupDivider(isTablet = isTablet)
                StatRow("Completed", stats.completed.toString(), isTablet)
                SettingsGroupDivider(isTablet = isTablet)
                StatRow("Total watch time", formatHours(stats.watchedHours), isTablet)
            }
        }
    }

    item {
        val progressState by WatchProgressRepository.uiState.collectAsStateWithLifecycle()
        val stats = statsOf(progressState.entries)
        if (stats.isEmpty) {
            SettingsSection(title = "No data yet", isTablet = isTablet) {
                SettingsGroup(isTablet = isTablet) {
                    StatRow("Play something and your statistics will show up here.", "", isTablet)
                }
            }
        }
    }
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
