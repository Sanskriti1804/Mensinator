package com.mensinator.app.core.domain.repository

import com.mensinator.app.core.domain.model.Period
import com.mensinator.app.core.domain.model.Symptom
import com.mensinator.app.core.util.Result
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth

/**
 * Repository interface for period-related data operations
 */
interface IPeriodRepository {
    
    /**
     * Get all periods
     */
    suspend fun getAllPeriods(): Result<List<Period>>
    
    /**
     * Get period by ID
     */
    suspend fun getPeriodById(id: Long): Result<Period?>
    
    /**
     * Get periods for a specific month
     */
    suspend fun getPeriodsForMonth(year: Int, month: Int): Result<List<Period>>
    
    /**
     * Get periods within a date range
     */
    suspend fun getPeriodsInRange(startDate: LocalDate, endDate: LocalDate): Result<List<Period>>
    
    /**
     * Insert or update a period
     */
    suspend fun savePeriod(period: Period): Result<Long>
    
    /**
     * Delete a period
     */
    suspend fun deletePeriod(id: Long): Result<Boolean>
    
    /**
     * Get all symptoms
     */
    suspend fun getAllSymptoms(): Result<List<Symptom>>
    
    /**
     * Get active symptoms
     */
    suspend fun getActiveSymptoms(): Result<List<Symptom>>
    
    /**
     * Get active symptoms for a specific date
     */
    suspend fun getSymptomsForDate(date: LocalDate): Result<List<Symptom>>
    
    /**
     * Save symptoms for a specific date
     */
    suspend fun saveSymptomsForDate(date: LocalDate, symptoms: List<Symptom>): Result<Boolean>
    
    /**
     * Get period prediction date
     */
    suspend fun getPredictedPeriodDate(): Result<LocalDate?>
    
    /**
     * Get ovulation prediction date
     */
    suspend fun getPredictedOvulationDate(): Result<LocalDate?>
    
    /**
     * Get cycle statistics
     */
    suspend fun getCycleStatistics(): Result<CycleStatistics>
}

/**
 * Data class for cycle statistics
 */
data class CycleStatistics(
    val averageCycleLength: Double,
    val averagePeriodLength: Double,
    val shortestCycle: Int,
    val longestCycle: Int,
    val totalCycles: Int
)
