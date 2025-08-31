package com.mensinator.app.core.domain.usecase

import com.mensinator.app.core.domain.model.Period
import com.mensinator.app.core.domain.repository.IPeriodRepository
import com.mensinator.app.core.util.Result

/**
 * Use case for saving period data
 */
class SavePeriodUseCase(
    private val periodRepository: IPeriodRepository
) {
    
    /**
     * Save a period (insert or update)
     */
    suspend operator fun invoke(period: Period): Result<Long> {
        return periodRepository.savePeriod(period)
    }
    
    /**
     * Delete a period
     */
    suspend operator fun invoke(id: Long): Result<Boolean> {
        return periodRepository.deletePeriod(id)
    }
}
