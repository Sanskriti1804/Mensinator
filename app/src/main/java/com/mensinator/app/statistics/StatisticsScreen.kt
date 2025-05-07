package com.mensinator.app.statistics

import HormoneCycleChart
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mensinator.app.R
import com.mensinator.app.ui.navigation.displayCutoutExcludingStatusBarsPadding
import com.mensinator.app.ui.theme.MensinatorTheme
import com.mensinator.app.ui.theme.appDRed
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = koinViewModel()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value

    val textApp = FontFamily(Font(R.font.textfont))
    val subTextApp = FontFamily(Font(R.font.secfont, FontWeight.SemiBold))

    LaunchedEffect(Unit) {
        viewModel.refreshData()
    }

    StatisticsScreenContent(
        modifier = modifier,
        state = state,
        textApp = textApp,
        subTextApp = subTextApp
    )
}

@Composable
fun StatisticsScreenContent(
    modifier: Modifier,
    state: StatisticsViewModel.ViewState,
    textApp: FontFamily,
    subTextApp: FontFamily
) {
    var showDialog by remember { mutableStateOf(false) }

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
        color = Color.White
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
                // Graph section
                Card(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                        .height(maxOf(chartHeight, minChartHeight)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = MaterialTheme.shapes.medium


                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyRow(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            item {
                                HormoneCycleChart(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(this@BoxWithConstraints.maxWidth * 3),
                                    periodStartDate = LocalDate.now()
                                )
                            }
                        }
                    }
                }

                OutlinedButton(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .height(48.dp),
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(1.dp, appDRed),
                    colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = appDRed // Text color set to appDRed
                    )
                ) {
                    Text(
                        text = "View Hormone Information",
                        fontFamily = textApp,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                // Header
                Text(
                    text = "Your Stats",
                    fontFamily = textApp,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                )

                // Stats cards
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
                            textFont = textApp,
                            subTextFont = subTextApp,
                            isHighlighted = label == stringResource(id = R.string.period_count),
                            modifier = Modifier
                                .width(110.dp)
                                .height(160.dp)
                        )
                    }
                }
            }
        }
    }
    if (showDialog) {
        ScrollableDialog(
            onDismissRequest = { showDialog = false },
//            textApp = textApp,
//            subTextApp = subTextApp
        )
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    textFont: FontFamily,
    subTextFont: FontFamily,
    isHighlighted: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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
                fontFamily = textFont,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = label,
                fontFamily = subTextFont,
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
            modifier = Modifier,
            state = StatisticsViewModel.ViewState(
                trackedPeriods = "2",
                averageCycleLength = "27.0 days",
                averagePeriodLength = "1.0 days",
                periodPredictionDate = "May 18, 2025",
                ovulationCount = "2",
                ovulationPredictionDate = "-",
                follicleGrowthDays = "0.0",
                averageLutealLength = "7.0 days"
            ),
            textApp = FontFamily.Default,
            subTextApp = FontFamily.Default
        )
    }
}