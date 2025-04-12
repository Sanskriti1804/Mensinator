package com.mensinator.app.hormonalinsights

import android.os.DropBoxManager
import com.mensinator.app.business.CalculationsHelper
import com.mensinator.app.business.IPeriodDatabaseHelper
import java.security.KeyStore
import java.time.LocalDate

class HormonalChartViewModel(
    private val calculationsHelper: CalculationsHelper,
    private val dbHelper: IPeriodDatabaseHelper
) {

    fun getHormonalData(): List<KeyStore.Entry> {
        val hormonalData = mutableListOf<KeyStore.Entry>()

        // Fetch cycle lengths and hormonal phases (e.g., luteal, ovulation)
        val cycleLengths = calculationsHelper.getCycleLengths() // Fetch your cycle lengths
        val ovulationDates = dbHelper.getLatestXOvulationsWithPeriod(5) // Get ovulation dates

        // Example: Add data points for each ovulation
        ovulationDates.forEachIndexed { index, ovulationDate ->
            // This assumes cycle length data and ovulation date are aligned
            val hormoneLevel = calculateHormoneLevelForCycle(ovulationDate, cycleLengths[index])
            hormonalData.add(DropBoxManager.Entry(index.toFloat(), hormoneLevel))
        }

        return hormonalData
    }

    private fun calculateHormoneLevelForCycle(ovulationDate: LocalDate, cycleLength: Long): Float {
        // You can calculate hormone levels based on the cycle length or phase
        // For simplicity, let’s assume it’s a random value for this example
        return (cycleLength % 10).toFloat()  // Just an example, replace with your own logic
    }
}
