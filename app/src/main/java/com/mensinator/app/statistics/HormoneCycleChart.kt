import android.graphics.Color
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate

@Composable
fun HormoneCycleChart(
    periodStartDate: LocalDate,
    modifier: Modifier = Modifier
) {
    // Generate 28 days of dates
    val cycleDays = List(28) { day -> periodStartDate.plusDays(day.toLong()) }

    // Generate hormone data for 28-day cycle
    val estrogenLevels = (0..27).map { day ->
        Entry(day.toFloat(), simulateEstrogen(day).toFloat())
    }
    val fshLevels = (0..27).map { day ->
        Entry(day.toFloat(), simulateFSH(day).toFloat())
    }
    val lhLevels = (0..27).map { day ->
        Entry(day.toFloat(), simulateLH(day).toFloat())
    }
    val progesteroneLevels = (0..27).map { day ->
        Entry(day.toFloat(), simulateProgesterone(day).toFloat())
    }
    val testosteroneLevels = (0..27).map { day ->
        Entry(day.toFloat(), simulateTestosterone(day).toFloat())
    }

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        AndroidView(
            factory = { context ->
                LineChart(context).apply {
                    // Configure datasets
                    val estrogenDataSet = LineDataSet(estrogenLevels, "").apply {
                        color = Color.rgb(255, 102, 178)  // Pink
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        setDrawCircles(false)
                        lineWidth = 3f
                    }

                    val fshDataSet = LineDataSet(fshLevels, "").apply {
                        color = Color.rgb(76, 175, 80)  // Green
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        setDrawCircles(false)
                        lineWidth = 3f
                    }

                    val lhDataSet = LineDataSet(lhLevels, "").apply {
                        color = Color.rgb(33, 150, 243)  // Blue
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        setDrawCircles(false)
                        lineWidth = 3f
                    }

                    val progesteroneDataSet = LineDataSet(progesteroneLevels, "").apply {
                        color = Color.rgb(156, 39, 176)  // Purple
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        setDrawCircles(false)
                        lineWidth = 3f
                    }

                    val testosteroneDataSet = LineDataSet(testosteroneLevels, "").apply {
                        color = Color.rgb(255, 152, 0)  // Orange
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        setDrawCircles(false)
                        lineWidth = 3f
                    }

                    data = LineData(
                        estrogenDataSet,
                        fshDataSet,
                        lhDataSet,
                        progesteroneDataSet,
                        testosteroneDataSet
                    )

                    // Minimal chart configuration
                    description.isEnabled = false
                    legend.isEnabled = false
                    setDrawGridBackground(false)
                    setDrawBorders(false)
                    setViewPortOffsets(0f, 0f, 0f, 0f)

                    // X-axis configuration (dates only)
                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawAxisLine(true)
                        setDrawGridLines(false)
                        axisLineColor = Color.LTGRAY
                        textColor = Color.DKGRAY
                        textSize = 10f

                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return if (value in 0f..27f) {
                                    cycleDays[value.toInt()].dayOfMonth.toString()
                                } else ""
                            }
                        }

                        setLabelCount(7, true)
                        granularity = 1f
                    }

                    // Disable other axes
                    axisLeft.isEnabled = false
                    axisRight.isEnabled = false

                    // Enable touch interaction
                    setTouchEnabled(true)
                    isDragEnabled = true
                    setScaleEnabled(true)

                    // Make chart horizontally scrollable
                    layoutParams = ViewGroup.LayoutParams(
                        (28 * 50).toInt(), // Wider than screen width
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier
                .fillMaxHeight()
                .width((28 * 50).dp) // Match the layoutParams width
        )
    }
}

// Keep all existing hormone simulation functions exactly the same
private fun simulateEstrogen(day: Int): Double {
    return when {
        day < 7 -> 1.0 + (day * 0.7)   // Follicular rise
        day == 13 -> 8.0                // Ovulation peak
        day < 21 -> 5.0 - (day - 13) * 0.4  // Post-ovulation decline
        else -> 1.5                     // Luteal baseline
    }
}

private fun simulateFSH(day: Int): Double {
    return when {
        day < 5 -> 3.0 + day * 0.5
        day == 13 -> 7.0
        day < 21 -> 4.0 - (day - 13) * 0.3
        else -> 2.5
    }
}

private fun simulateLH(day: Int): Double {
    return when {
        day < 10 -> 2.0
        day == 13 -> 12.0  // LH surge
        day < 16 -> 5.0
        else -> 1.5
    }
}

private fun simulateProgesterone(day: Int): Double {
    return when {
        day < 14 -> 0.5
        day < 21 -> 1.0 + (day - 14) * 1.2  // Luteal rise
        else -> 8.0 - (day - 21) * 0.5
    }
}

private fun simulateTestosterone(day: Int): Double {
    return 3.0 + 0.3 * sin(day * 0.2)
}

private fun sin(x: Double): Double = kotlin.math.sin(x)