import android.graphics.Color
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HormoneCycleChart(
    periodStartDate: LocalDate,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val chartWidth = remember { 3600.dp } // Wide enough to show all days

    // Generate dates for 28-day cycle starting from periodStartDate
    val cycleDates = (0..27).map { day -> periodStartDate.plusDays(day.toLong()) }

    // Generate hormone data for 28-day cycle mapped to actual dates
    val estrogenLevels = cycleDates.mapIndexed { index, date ->
        Entry(index.toFloat(), simulateEstrogen(index).toFloat())
    }

    val progesteroneLevels = cycleDates.mapIndexed { index, date ->
        Entry(index.toFloat(), simulateProgesterone(index).toFloat())
    }

    val lhLevels = cycleDates.mapIndexed { index, date ->
        Entry(index.toFloat(), simulateLH(index).toFloat())
    }

    val fshLevels = cycleDates.mapIndexed { index, date ->
        Entry(index.toFloat(), simulateFSH(index).toFloat())
    }

    val testosteroneLevels = cycleDates.mapIndexed { index, date ->
        Entry(index.toFloat(), simulateTestosterone(index).toFloat())
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .horizontalScroll(scrollState)
//            .background(Color.WHITE.toInt())
    ) {
        AndroidView(
            factory = { context ->
                LineChart(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    // Configure datasets (keep existing styles)
                    val estrogenDataSet = LineDataSet(estrogenLevels, "").apply {
                        color = Color.rgb(255, 102, 178)
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        setDrawCircles(false)
                        lineWidth = 3f
                    }

                    val progesteroneDataSet = LineDataSet(progesteroneLevels, "").apply {
                        color = Color.rgb(156, 39, 176)
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        setDrawCircles(false)
                        lineWidth = 3f
                    }

                    val lhDataSet = LineDataSet(lhLevels, "").apply {
                        color = Color.rgb(33, 150, 243)
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        setDrawCircles(false)
                        lineWidth = 3f
                    }

                    val fshDataSet = LineDataSet(fshLevels, "").apply {
                        color = Color.rgb(76, 175, 80)
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        setDrawCircles(false)
                        lineWidth = 3f
                    }

                    val testosteroneDataSet = LineDataSet(testosteroneLevels, "").apply {
                        color = Color.rgb(255, 152, 0)
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        setDrawCircles(false)
                        lineWidth = 3f
                    }

                    data = LineData(estrogenDataSet, progesteroneDataSet, lhDataSet, fshDataSet, testosteroneDataSet)

                    // X-axis configuration with actual dates
                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawAxisLine(true)
                        setDrawGridLines(false)
                        axisLineColor = Color.LTGRAY
                        textColor = Color.DKGRAY
                        textSize = 10f
                        granularity = 1f
                        setCenterAxisLabels(false)
                        yOffset = 10f

                        valueFormatter = object : ValueFormatter() {
                            private val dateFormatter = DateTimeFormatter.ofPattern("MMM d")

                            override fun getFormattedValue(value: Float): String {
                                val dayIndex = value.toInt()
                                if (dayIndex in cycleDates.indices) {
                                    return cycleDates[dayIndex].format(dateFormatter)
                                }
                                return ""
                            }
                        }

                        setLabelCount(28, false)
                        axisMinimum = -0.5f
                        axisMaximum = 27.5f
                    }

                    // Chart configuration (keep existing settings)
                    description.isEnabled = false
                    legend.isEnabled = false
                    axisLeft.isEnabled = false
                    axisRight.isEnabled = false
                    setDrawGridBackground(false)
                    setDrawBorders(false)
                    extraBottomOffset = 30f
                    setViewPortOffsets(30f, 30f, 30f, 80f)
                    setTouchEnabled(true)
                    setDragEnabled(true)
                    isDragXEnabled = true
                    isDragYEnabled = false
                    setScaleEnabled(false)

                    invalidate()
                }
            },
            modifier = Modifier
                .fillMaxHeight()
                .width(chartWidth)
        )
    }

    // Auto-scroll to current day in the cycle
    LaunchedEffect(periodStartDate) {
        val today = LocalDate.now()
        val daysPassed = periodStartDate.until(today).days
        if (daysPassed in 0..27) {
            val scrollToPosition = (daysPassed * 120).toFloat() // Adjust multiplier as needed
            scrollState.scrollTo(scrollToPosition.toInt())
        }
    }
}

// Simulation functions remain unchanged
private fun simulateEstrogen(day: Int): Double {
    return when {
        day < 7 -> 1.0 + (day * 0.7)
        day == 14 -> 8.0
        day < 21 -> 5.0 - (day - 14) * 0.4
        else -> 1.5
    }
}

private fun simulateProgesterone(day: Int): Double {
    return when {
        day < 15 -> 0.5
        day < 22 -> 1.0 + (day - 15) * 1.2
        else -> 8.0 - (day - 22) * 0.5
    }
}

private fun simulateLH(day: Int): Double {
    return when {
        day < 11 -> 2.0
        day == 14 -> 12.0
        day < 17 -> 5.0
        else -> 1.5
    }
}

private fun simulateFSH(day: Int): Double {
    return when {
        day < 5 -> 3.0 + day * 0.5
        day == 14 -> 7.0
        day < 21 -> 4.0 - (day - 14) * 0.3
        else -> 2.5
    }
}

private fun simulateTestosterone(day: Int): Double {
    return 3.0 + 0.3 * sin(day * 0.2)
}

private fun sin(x: Double): Double = kotlin.math.sin(x)