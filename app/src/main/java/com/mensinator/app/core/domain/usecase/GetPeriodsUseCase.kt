package com.mensinator.app.core.domain.usecase

import com.mensinator.app.core.domain.model.Period
import com.mensinator.app.core.domain.repository.IPeriodRepository
import com.mensinator.app.core.util.Result
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth

/**
 * Use case for retrieving period data
 */
class GetPeriodsUseCase(
    private val periodRepository: IPeriodRepository
) {
    
    /**
     * Get all periods
     */
    suspend operator fun invoke(): Result<List<Period>> {
        return periodRepository.getAllPeriods()
    }
    
    /**
     * Get periods for a specific month
     */
    suspend operator fun invoke(year: Int, month: Int): Result<List<Period>> {
        return periodRepository.getPeriodsForMonth(year, month)
    }
    
    /**
     * Get periods within a date range
     */
    suspend operator fun invoke(startDate: LocalDate, endDate: LocalDate): Result<List<Period>> {
        return periodRepository.getPeriodsInRange(startDate, endDate)
    }
    
    /**
     * Get period by ID
     */
    suspend operator fun invoke(id: Long): Result<Period?> {
        return periodRepository.getPeriodById(id)
    }
    
    /**
     * Get symptoms for a specific date
     */
    suspend fun getSymptomsForDate(date: LocalDate): Result<List<com.mensinator.app.core.domain.model.Symptom>> {
        return periodRepository.getSymptomsForDate(date)
    }
    
    /**
     * Get active symptoms
     */
    suspend fun getActiveSymptoms(): Result<List<com.mensinator.app.core.domain.model.Symptom>> {
        return periodRepository.getActiveSymptoms()
    }
}
