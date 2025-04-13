package com.mensinator.app.business

import android.util.Log
import com.mensinator.app.extensions.roundToTwoDecimalPoints
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

abstract class CalculationsHelper(
    private val dbHelper: IPeriodDatabaseHelper,
) : ICalculationsHelper {
    private val periodHistory
        get() = runBlocking {
            dbHelper.getSettingByKey("period_history")?.value?.toIntOrNull() ?: 5
        }
    private val ovulationHistory
        get() = runBlocking {
            dbHelper.getSettingByKey("ovulation_history")?.value?.toIntOrNull() ?: 5
        }
    private val lutealCalculation
        get() = runBlocking {
            dbHelper.getSettingByKey("luteal_period_calculation")?.value?.toIntOrNull() ?: 0
        }

    override fun calculateNextPeriod(): LocalDate? {
        return if (lutealCalculation == 1) {
            Log.d("TAG", "Advanced calculation")
            advancedNextPeriod()
        } else {
            Log.d("TAG", "Basic calculation")
            val listPeriodDates = dbHelper.getLatestXPeriodStart(periodHistory)

            if (listPeriodDates.isEmpty()) {
                null
            } else {
                val cycleLengths = mutableListOf<Long>()
                for (i in 0 until listPeriodDates.size - 1) {
                    val cycleLength = java.time.temporal.ChronoUnit.DAYS.between(
                        listPeriodDates[i],
                        listPeriodDates[i + 1]
                    )
                    cycleLengths.add(cycleLength)
                }
                val averageLength = cycleLengths.average()

                listPeriodDates.last().plusDays(averageLength.toLong())
            }.also {
                Log.d("TAG", "Expected period date Basic: $it")
            }
        }
    }

    private fun advancedNextPeriod(): LocalDate? {
        val ovulationDates = dbHelper.getLatestXOvulationsWithPeriod(ovulationHistory)
        if (ovulationDates.isEmpty()) {
            Log.d("TAG", "Ovulationdates are empty")
            return null
        }

        var lutealLength = 0
        for (date in ovulationDates) {
            val lutealDay = getLutealLengthForPeriod(date)
            lutealLength += lutealDay
            Log.d("TAG", "Luteal for date $date: $lutealDay")
        }

        val averageLutealLength = lutealLength / ovulationDates.size
        val lastOvulation = dbHelper.getLastOvulation()
        if (lastOvulation == null) {
            Log.d("TAG", "Ovulation is null")
            return null
        }

        val periodDates = dbHelper.getLatestXPeriodStart(ovulationHistory)
        if (periodDates.isNotEmpty() && periodDates.last() > lastOvulation) {
            val avgGrowthRate = averageFollicalGrowthInDays()
            val expectedOvulation = periodDates.last().plusDays(avgGrowthRate.toInt().toLong())
            val expectedPeriodDate = expectedOvulation.plusDays(averageLutealLength.toLong())
            return expectedPeriodDate
        } else {
            val expectedPeriodDate = lastOvulation.plusDays(averageLutealLength.toLong())
            return expectedPeriodDate
        }
    }

    override fun averageFollicalGrowthInDays(): Double {
        val ovulationDates = dbHelper.getXLatestOvulationsDates(ovulationHistory)
        if (ovulationDates.isEmpty()) {
            return 0.0
        } else {
            val growthRate = mutableListOf<Int>()
            for (d in ovulationDates) {
                val firstDatePreviousPeriod = dbHelper.getFirstPreviousPeriodDate(d)
                if (firstDatePreviousPeriod != null) {
                    growthRate.add(
                        d.toEpochDay().toInt() - firstDatePreviousPeriod.toEpochDay().toInt()
                    )
                }
            }
            if (growthRate.isEmpty()) {
                return 0.0
            }
            return growthRate.average().roundToTwoDecimalPoints()
        }
    }

    override fun averageCycleLength(): Double {
        val periodDates = dbHelper.getLatestXPeriodStart(periodHistory)

        if (periodDates.size < 2) {
            return 0.0
        }

        val cycleLengths = mutableListOf<Long>()
        for (i in 0 until periodDates.size - 1) {
            val cycleLength =
                java.time.temporal.ChronoUnit.DAYS.between(periodDates[i], periodDates[i + 1])
            cycleLengths.add(cycleLength)
        }

        val cycleLengthAverage = cycleLengths.average()

        return cycleLengthAverage
    }

    override fun averagePeriodLength(): Double {
        val daysInPeriod = mutableListOf<Int>()
        val periodDates = dbHelper.getLatestXPeriodStart(periodHistory)

        for (i in 0 until periodDates.size - 1) {
            val numberOfDates = dbHelper.getNoOfDatesInPeriod(periodDates[i])
            if (numberOfDates > 0) {
                daysInPeriod.add(numberOfDates)
            }
        }

        return if (daysInPeriod.isNotEmpty()) {
            daysInPeriod.average()
        } else {
            0.0
        }
    }

    override fun averageLutealLength(): Double {
        val lutealLengths = mutableListOf<Long>()
        val ovulationDates = dbHelper.getLatestXOvulationsWithPeriod(ovulationHistory)

        for (ovulationDate in ovulationDates) {
            val nextPeriodDate = dbHelper.getFirstNextPeriodDate(ovulationDate)

            if (nextPeriodDate != null) {
                val daysBetween =
                    java.time.temporal.ChronoUnit.DAYS.between(ovulationDate, nextPeriodDate)
                lutealLengths.add(daysBetween)
            } else {
                Log.d("TAG", "Next period date is null")
                return 0.0
            }
        }

        return lutealLengths.average()
    }

    override fun getLutealLengthForPeriod(date: LocalDate): Int {
        val firstNextPeriodDate = dbHelper.getFirstNextPeriodDate(date)
        if (firstNextPeriodDate != null) {
            val lutealLength =
                java.time.temporal.ChronoUnit.DAYS.between(date, firstNextPeriodDate).toInt()
            Log.d("TAG", "Luteal for single date PDH $firstNextPeriodDate $date: $lutealLength")
            return lutealLength
        } else {
            return 0
        }
    }

    // Implementing getCycleLengths() to resolve the error
    override fun getCycleLengths(): Any {
        val periodDates = dbHelper.getLatestXPeriodStart(periodHistory)

        if (periodDates.size < 2) {
            return emptyList<Long>()
        }

        val cycleLengths = mutableListOf<Long>()
        for (i in 0 until periodDates.size - 1) {
            val cycleLength =
                java.time.temporal.ChronoUnit.DAYS.between(periodDates[i], periodDates[i + 1])
            cycleLengths.add(cycleLength)
        }

        return cycleLengths
    }
}
