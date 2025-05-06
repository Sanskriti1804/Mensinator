import android.graphics.Color
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
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
    // Generate hormone data for 30-day cycle
    val estrogenLevels = (0..29).map { day ->
        Entry(day.toFloat(), simulateEstrogen(day).toFloat())
    }
    val progesteroneLevels = (0..29).map { day ->
        Entry(day.toFloat(), simulateProgesterone(day).toFloat())
    }
    val lhLevels = (0..29).map { day ->
        Entry(day.toFloat(), simulateLH(day).toFloat())
    }
    val fshLevels = (0..29).map { day ->
        Entry(day.toFloat(), simulateFSH(day).toFloat())
    }
    val testosteroneLevels = (0..29).map { day ->
        Entry(day.toFloat(), simulateTestosterone(day).toFloat())
    }

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                // Configure datasets
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

                // X-axis configuration
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawAxisLine(true)
                    setDrawGridLines(false)
                    axisLineColor = Color.LTGRAY
                    textColor = Color.DKGRAY
                    textSize = 10f
                    granularity = 1f
                    setCenterAxisLabels(false) // Prevents label jumping
                    yOffset = 10f // Space between axis and labels

                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return (value.toInt() + 1).toString()
                        }
                    }

                    setLabelCount(30, true)
                    axisMinimum = -0.5f
                    axisMaximum = 29.5f
                }

                // Chart configuration
                description.isEnabled = false
                legend.isEnabled = false // Completely removed legend

                axisLeft.apply {
                    isEnabled = true
                    setDrawGridLines(true)
                    gridColor = Color.LTGRAY
                }
                axisRight.isEnabled = false

                // Padding and scrolling fixes
                extraBottomOffset = 80f // Proper padding below X-axis
                setViewPortOffsets(30f, 30f, 30f, 100f) // Balanced padding

                // Scrolling behavior
                setTouchEnabled(true)
                setDragEnabled(true)
                isDragXEnabled = true
                isDragYEnabled = false
                setScaleEnabled(false) // Disable zooming

                invalidate() // Force redraw
            }
        },
        modifier = modifier
            .fillMaxHeight()
            .width(3600.dp))
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