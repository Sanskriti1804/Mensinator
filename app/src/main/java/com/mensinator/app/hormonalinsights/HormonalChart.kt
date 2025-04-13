import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.mensinator.app.hormonalinsights.HormonalChartViewModel

@Composable
fun HormonalChart(
    viewModel: HormonalChartViewModel
) {
    val chartData = remember { mutableStateOf(emptyList<Entry>()) }

    LaunchedEffect(Unit) {
        chartData.value = viewModel.getHormonalData()
    }

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                setBackgroundColor(Color.WHITE)
                description.isEnabled = false
            }
        },
        update = { chart ->
            val dataSet = LineDataSet(chartData.value, "Hormonal Data").apply {
                color = Color.BLUE
                valueTextColor = Color.BLACK
                valueTextSize = 12f
            }
            chart.data = LineData(dataSet)
            chart.invalidate()
        },
        modifier = Modifier.fillMaxSize()
    )
}

