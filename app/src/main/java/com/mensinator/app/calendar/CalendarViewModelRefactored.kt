package com.mensinator.app.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mensinator.app.core.domain.model.Period
import com.mensinator.app.core.domain.model.Symptom
import com.mensinator.app.core.domain.usecase.GetPeriodsUseCase
import com.mensinator.app.core.domain.usecase.SavePeriodUseCase
import com.mensinator.app.core.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

/**
 * Refactored CalendarViewModel following MVVM architecture principles
 * Separates business logic from UI logic and uses proper state management
 */
class CalendarViewModelRefactored(
    private val getPeriodsUseCase: GetPeriodsUseCase,
    private val savePeriodUseCase: SavePeriodUseCase
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    // Events
    private val _events = MutableStateFlow<CalendarEvent?>(null)
    val events: StateFlow<CalendarEvent?> = _events.asStateFlow()

    init {
        loadInitialData()
    }

    /**
     * Load initial data for the calendar
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val currentMonth = YearMonth.now()
            loadDataForMonth(currentMonth.year, currentMonth.monthValue)
        }
    }

    /**
     * Load data for a specific month
     */
    fun loadDataForMonth(year: Int, month: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val periodsResult = getPeriodsUseCase(year, month)
                val symptomsResult = getPeriodsUseCase.getActiveSymptoms()
                
                when {
                    periodsResult.isError() -> {
                        val errorMessage = when (periodsResult) {
                            is Result.Error -> 
                                periodsResult.exception.message ?: "Unknown error occurred"
                            else -> "Unknown error occurred"
                        }
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                error = errorMessage
                            )
                        }
                    }
                    symptomsResult.isError() -> {
                        val errorMessage = when (symptomsResult) {
                            is Result.Error -> 
                                symptomsResult.exception.message ?: "Unknown error occurred"
                            else -> "Unknown error occurred"
                        }
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                error = errorMessage
                            )
                        }
                    }
                    else -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                focusedYearMonth = YearMonth.of(year, month),
                                periods = periodsResult.getOrNull() ?: emptyList(),
                                symptoms = symptomsResult.getOrNull() ?: emptyList(),
                                error = null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }

    /**
     * Select dates on the calendar
     */
    fun selectDates(dates: List<LocalDate>) {
        _uiState.update { it.copy(selectedDates = dates) }
        
        if (dates.isNotEmpty()) {
            loadSymptomsForDate(dates.last())
        }
    }

    /**
     * Load symptoms for a specific date
     */
    private fun loadSymptomsForDate(date: LocalDate) {
        viewModelScope.launch {
            try {
                val symptomsResult = getPeriodsUseCase.getSymptomsForDate(date)
                if (symptomsResult.isSuccess()) {
                    _uiState.update {
                        it.copy(symptomsForSelectedDate = symptomsResult.getOrNull() ?: emptyList())
                    }
                }
            } catch (e: Exception) {
                // Handle error silently for now, could be logged
            }
        }
    }

    /**
     * Save a period
     */
    fun savePeriod(period: Period) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            
            try {
                val result = savePeriodUseCase(period)
                if (result.isSuccess()) {
                    _events.value = CalendarEvent.PeriodSaved(period)
                    loadDataForMonth(
                        _uiState.value.focusedYearMonth.year,
                        _uiState.value.focusedYearMonth.monthValue
                    )
                } else {
                    _events.value = CalendarEvent.Error("Failed to save period")
                }
            } catch (e: Exception) {
                _events.value = CalendarEvent.Error(e.message ?: "An unexpected error occurred")
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    /**
     * Delete a period
     */
    fun deletePeriod(periodId: Long) {
        viewModelScope.launch {
            try {
                val result = savePeriodUseCase(periodId)
                if (result.isSuccess()) {
                    _events.value = CalendarEvent.PeriodDeleted(periodId)
                    loadDataForMonth(
                        _uiState.value.focusedYearMonth.year,
                        _uiState.value.focusedYearMonth.monthValue
                    )
                } else {
                    _events.value = CalendarEvent.Error("Failed to delete period")
                }
            } catch (e: Exception) {
                _events.value = CalendarEvent.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    /**
     * Clear error state
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    /**
     * Clear events
     */
    fun clearEvents() {
        _events.value = null
    }

    /**
     * Update theme preference
     */
    fun updateTheme(isDarkMode: Boolean) {
        _uiState.update { it.copy(isDarkMode = isDarkMode) }
    }
}

/**
 * UI State for the calendar screen
 */
data class CalendarUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val focusedYearMonth: YearMonth = YearMonth.now(),
    val selectedDates: List<LocalDate> = emptyList(),
    val periods: List<Period> = emptyList(),
    val symptoms: List<Symptom> = emptyList(),
    val symptomsForSelectedDate: List<Symptom> = emptyList(),
    val isDarkMode: Boolean = false,
    val error: String? = null
)

/**
 * Events that can be emitted from the ViewModel
 */
sealed class CalendarEvent {
    data class PeriodSaved(val period: Period) : CalendarEvent()
    data class PeriodDeleted(val periodId: Long) : CalendarEvent()
    data class Error(val message: String) : CalendarEvent()
}
