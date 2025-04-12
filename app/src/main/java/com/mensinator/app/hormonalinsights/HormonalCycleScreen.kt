package com.mensinator.app.hormonalinsights

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HormonalCycleScreen(viewModel: HormonalChartViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Hormonal Cycle Chart", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        // Display the chart
        HormonalChart(viewModel = viewModel)
    }
}
