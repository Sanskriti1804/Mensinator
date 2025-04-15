package com.mensinator.app.statistics

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.mensinator.app.ui.theme.EstrogenColor
import com.mensinator.app.ui.theme.FSHColor
import com.mensinator.app.ui.theme.LHColor
import com.mensinator.app.ui.theme.ProgesteroneColor
import com.mensinator.app.ui.theme.TestosteroneColor

@Composable
fun HormoneCycleChart(modifier: Modifier = Modifier) {
    // Sample hormone levels
    val estrogenLevels = listOf(
        Entry(0f, 1f), Entry(1f, 5f), Entry(2f, 6f), Entry(3f, 4f), Entry(4f, 1f)
    )
    val fshLevels = listOf(
        Entry(0f, 5f), Entry(1f, 7f), Entry(2f, 9f), Entry(3f, 6f), Entry(4f, 4f)
    )
    val lhLevels = listOf(
        Entry(0f, 3f), Entry(1f, 4f), Entry(2f, 10f), Entry(3f, 2f), Entry(4f, 1f)
    )
    val progesteroneLevels = listOf(
        Entry(0f, 0f), Entry(1f, 1f), Entry(2f, 4f), Entry(3f, 9f), Entry(4f, 6f)
    )
    val testosteroneLevels = listOf(
        Entry(0f, 3f), Entry(1f, 2f), Entry(2f, 3f), Entry(3f, 5f), Entry(4f, 4f)
    )

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                val estrogenDataSet = LineDataSet(estrogenLevels, "Estrogen").apply {
                    color = EstrogenColor.toArgb()
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    setDrawCircles(false)
                    setDrawFilled(true)
                    fillColor = EstrogenColor.toArgb()
                    fillAlpha = 100
                }

                val fshDataSet = LineDataSet(fshLevels, "FSH").apply {
                    color = FSHColor.toArgb()
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    setDrawCircles(false)
                    setDrawFilled(true)
                    fillColor = FSHColor.toArgb()
                    fillAlpha = 100
                }

                val lhDataSet = LineDataSet(lhLevels, "LH").apply {
                    color = LHColor.toArgb()
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    setDrawCircles(false)
                    setDrawFilled(true)
                    fillColor = LHColor.toArgb()
                    fillAlpha = 100
                }

                val progesteroneDataSet = LineDataSet(progesteroneLevels, "Progesterone").apply {
                    color = ProgesteroneColor.toArgb()
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    setDrawCircles(false)
                    setDrawFilled(true)
                    fillColor = ProgesteroneColor.toArgb()
                    fillAlpha = 100
                }

                val testosteroneDataSet = LineDataSet(testosteroneLevels, "Testosterone").apply {
                    color = TestosteroneColor.toArgb()
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    setDrawCircles(false)
                    setDrawFilled(true)
                    fillColor = TestosteroneColor.toArgb()
                    fillAlpha = 100
                }

                val lineData = LineData(
                    estrogenDataSet,
                    fshDataSet,
                    lhDataSet,
                    progesteroneDataSet,
                    testosteroneDataSet
                )
                data = lineData

                description.isEnabled = true
                description.text = "Hormone Levels Over Time (Cycle)"

                // Hide axis lines, gridlines, and markers
                axisLeft.apply {
                    setDrawAxisLine(false)
                    setDrawGridLines(false)
                    isEnabled = false
                }

                axisRight.isEnabled = false

                xAxis.apply {
                    setDrawAxisLine(false)
                    setDrawGridLines(false)
                    position = XAxis.XAxisPosition.BOTTOM
                    isEnabled = true
                    labelCount = 5  // Number of labels (hormone names)

                    // Adding hormone names in color at the bottom
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return when (value) {
                                0f -> "Estrogen"
                                1f -> "FSH"
                                2f -> "LH"
                                3f -> "Progesterone"
                                4f -> "Testosterone"
                                else -> ""
                            }
                        }
                    }
                }

                // Hide description and legend if needed
                description.isEnabled = false
                legend.isEnabled = false

                // Extra clean look, no grid background
                setDrawGridBackground(false)
                setDrawBorders(false)

                invalidate()
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
