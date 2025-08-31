package com.mensinator.app.core.util

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Utility class for date-related operations
 */
object DateUtils {
    
    /**
     * Default date formatter for display
     */
    val displayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    
    /**
     * Short date formatter for compact display
     */
    val shortFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd")
    
    /**
     * Month formatter for month display
     */
    val monthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    
    /**
     * Day of week formatter
     */
    val dayOfWeekFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE")
    
    /**
     * Format a date for display
     */
    fun formatDate(date: LocalDate, formatter: DateTimeFormatter = displayFormatter): String {
        return date.format(formatter)
    }
    
    /**
     * Format a date for short display
     */
    fun formatShortDate(date: LocalDate): String {
        return date.format(shortFormatter)
    }
    
    /**
     * Format a month for display
     */
    fun formatMonth(yearMonth: YearMonth): String {
        return yearMonth.format(monthFormatter)
    }
    
    /**
     * Get the number of days between two dates
     */
    fun daysBetween(startDate: LocalDate, endDate: LocalDate): Long {
        return ChronoUnit.DAYS.between(startDate, endDate)
    }
    
    /**
     * Check if a date is today
     */
    fun isToday(date: LocalDate): Boolean {
        return date == LocalDate.now()
    }
    
    /**
     * Check if a date is in the past
     */
    fun isPast(date: LocalDate): Boolean {
        return date < LocalDate.now()
    }
    
    /**
     * Check if a date is in the future
     */
    fun isFuture(date: LocalDate): Boolean {
        return date > LocalDate.now()
    }
    
    /**
     * Get the start of the week for a given date
     */
    fun getStartOfWeek(date: LocalDate): LocalDate {
        return date.minusDays(date.dayOfWeek.value.toLong() - 1)
    }
    
    /**
     * Get the end of the week for a given date
     */
    fun getEndOfWeek(date: LocalDate): LocalDate {
        return getStartOfWeek(date).plusDays(6)
    }
    
    /**
     * Get the start of the month for a given date
     */
    fun getStartOfMonth(date: LocalDate): LocalDate {
        return date.withDayOfMonth(1)
    }
    
    /**
     * Get the end of the month for a given date
     */
    fun getEndOfMonth(date: LocalDate): LocalDate {
        return date.withDayOfMonth(date.lengthOfMonth())
    }
    
    /**
     * Get all dates in a month
     */
    fun getDatesInMonth(yearMonth: YearMonth): List<LocalDate> {
        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()
        
        val dates = mutableListOf<LocalDate>()
        var currentDate = startDate
        
        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }
        
        return dates
    }
    
    /**
     * Get all dates in a range
     */
    fun getDatesInRange(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
        val dates = mutableListOf<LocalDate>()
        var currentDate = startDate
        
        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }
        
        return dates
    }
    
    /**
     * Calculate age based on birth date
     */
    fun calculateAge(birthDate: LocalDate): Int {
        val today = LocalDate.now()
        return ChronoUnit.YEARS.between(birthDate, today).toInt()
    }
    
    /**
     * Get the next occurrence of a date (for recurring events)
     */
    fun getNextOccurrence(baseDate: LocalDate, intervalDays: Int): LocalDate {
        val today = LocalDate.now()
        var nextDate = baseDate
        
        while (nextDate.isBefore(today)) {
            nextDate = nextDate.plusDays(intervalDays.toLong())
        }
        
        return nextDate
    }
    
    /**
     * Check if two date ranges overlap
     */
    fun doRangesOverlap(
        start1: LocalDate,
        end1: LocalDate,
        start2: LocalDate,
        end2: LocalDate
    ): Boolean {
        return start1 <= end2 && start2 <= end1
    }
}
