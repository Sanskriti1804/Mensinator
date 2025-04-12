package com.mensinator.app.hormonalinsights

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import java.lang.reflect.Modifier
import java.security.KeyStore
import java.util.Collections.emptyList

@Composable
fun HormonalChart(
    viewModel: HormonalChartViewModel
) {
    val chartData = remember { mutableStateOf(emptyList<KeyStore.Entry>()) }

    // Fetch the hormonal data from ViewModel
    LaunchedEffect(Unit) {
        chartData.value = viewModel.getHormonalData()
    }

    AndroidView(
        factory = { context ->
            // Initialize MPAndroidChart LineChart
            val chart = LineChart(context)

            // Prepare data set for the chart
            val dataSet = LineDataSet(chartData.value, "Hormonal Data")
            dataSet.color = Color.BLUE
            dataSet.valueTextColor = Color.BLACK
            dataSet.valueTextSize = 12f

            // Create a LineData object
            val data = LineData(dataSet)
            chart.data = data
            chart.invalidate() // Refresh the chart

            // Customize chart appearance (optional)
            chart.setBackgroundColor(Color.WHITE)
            chart.description.isEnabled = false

            chart
        },
        modifier = Modifier.fillMaxSize()
    )
}
