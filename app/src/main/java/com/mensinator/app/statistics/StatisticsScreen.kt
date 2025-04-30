package com.mensinator.app.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .displayCutoutExcludingStatusBarsPadding()
        ) {
            val chartHeight = this.maxHeight * 0.65f
            val minChartHeight = 250.dp

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Graph section - full width Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(maxOf(chartHeight, minChartHeight)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(1.dp)
                    ) {
                        HormoneCycleChart(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // Title
                Text(
                    text = "Your Stats",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                )

                // Horizontal scrolling stats using LazyRow
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(stats) { (value, label) ->
                        StatCard(
                            value = value ?: "-",
                            label = label,
                            isHighlighted = label == stringResource(id = R.string.period_count),
                            modifier = Modifier
                                .width(110.dp)  // Reduced width
                                .height(160.dp) // Increased height
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    isHighlighted: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
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
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StatisticsScreenPreview() {
    MensinatorTheme {
        StatisticsScreenContent(
            state = StatisticsViewModel.ViewState(
                trackedPeriods = "2",
                averageCycleLength = "27.0 days",
                averagePeriodLength = "1.0 days",
                periodPredictionDate = "May 18, 2025",
                ovulationCount = "2",
                ovulationPredictionDate = "-",
                follicleGrowthDays = "0.0",
                averageLutealLength = "7.0 days"
            )
        )
    }
}