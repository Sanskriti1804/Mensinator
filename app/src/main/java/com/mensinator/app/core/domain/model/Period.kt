package com.mensinator.app.core.domain.model

import java.time.LocalDate

/**
 * Domain model representing a menstrual period
 */
data class Period(
    val id: Long,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val flow: FlowIntensity,
    val symptoms: List<Symptom>,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Enum representing the intensity of menstrual flow
 */
enum class FlowIntensity(val value: Int, val label: String) {
    LIGHT(1, "Light"),
    MEDIUM(2, "Medium"),
    HEAVY(3, "Heavy"),
    SPOTTING(0, "Spotting");

    companion object {
        fun fromValue(value: Int): FlowIntensity = values().find { it.value == value } ?: MEDIUM
    }
}

/**
 * Domain model representing a symptom
 */
data class Symptom(
    val id: Long,
    val name: String,
    val isActive: Boolean,
    val color: String,
    val category: SymptomCategory
)

/**
 * Enum representing symptom categories
 */
enum class SymptomCategory(val label: String) {
    PHYSICAL("Physical"),
    EMOTIONAL("Emotional"),
    DIGESTIVE("Digestive"),
    OTHER("Other")
}
