package com.mensinator.app.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mensinator.app.R
import com.mensinator.app.ui.navigation.displayCutoutExcludingStatusBarsPadding
import com.mensinator.app.ui.theme.MensinatorTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = koinViewModel(),
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.refreshData()
    }

    StatisticsScreenContent(modifier, state)
}

@Composable
private fun StatisticsScreenContent(
    modifier: Modifier = Modifier,
    state: StatisticsViewModel.ViewState
) {
    val stats = listOf(
        state.trackedPeriods to stringResource(id = R.string.period_count),
        state.averageCycleLength to stringResource(id = R.string.average_cycle_length),
        state.averagePeriodLength to stringResource(id = R.string.average_period_length),
        state.periodPredictionDate to stringResource(id = R.string.next_period_start_future),
        state.ovulationCount to stringResource(id = R.string.ovulation_count),
        state.follicleGrowthDays to stringResource(id = R.string.average_ovulation_day),
        state.ovulationPredictionDate to stringResource(id = R.string.next_predicted_ovulation),
        state.averageLutealLength to stringResource(id = R.string.average_luteal_length)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .displayCutoutExcludingStatusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(stats) { (value, label) ->
                StatCard(value = value ?: "-", label = label)
            }
        }
    }
}

@Composable
fun StatCard(value: String, label: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.4f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticsScreenPreview() {
    MensinatorTheme {
        StatisticsScreenContent(
            state = StatisticsViewModel.ViewState(
                trackedPeriods = "3",
                averageCycleLength = "28.5 days",
                averagePeriodLength = "5.0 days",
                periodPredictionDate = "28 Feb 2024",
                ovulationCount = "4",
                ovulationPredictionDate = "20 Mar 2024",
                follicleGrowthDays = "14.0",
                averageLutealLength = "15.0 days"
            )
        )
    }
}
