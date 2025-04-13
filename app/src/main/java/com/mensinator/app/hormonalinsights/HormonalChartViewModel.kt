package com.mensinator.app.hormonalinsights

import com.github.mikephil.charting.data.Entry
import com.mensinator.app.business.CalculationsHelper
import com.mensinator.app.business.IPeriodDatabaseHelper
import java.time.LocalDate

class HormonalChartViewModel(
    private val calculationsHelper: CalculationsHelper,
    private val dbHelper: IPeriodDatabaseHelper
) {

    fun getHormonalData(): List<Entry> {
        val hormonalData = mutableListOf<Entry>()

        val cycleLengths = calculationsHelper.getCycleLengths()
        val ovulationDates = dbHelper.getLatestXOvulationsWithPeriod(5)

//        ovulationDates.forEachIndexed { index, ovulationDate ->
//            val hormoneLevel =
//                calculateHormoneLevelForCycle(ovulationDate, cycleLengths.getOrNull(index) ?: 28)
//            hormonalData.add(Entry(index.toFloat(), hormoneLevel))
//        }

        return hormonalData
    }

    private fun calculateHormoneLevelForCycle(ovulationDate: LocalDate, cycleLength: Long): Float {
        // Just a simple example for now
        return (cycleLength % 10).toFloat()
    }
}
