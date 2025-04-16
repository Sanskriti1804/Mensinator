package com.mensinator.app.questionnaire

enum class QuestionType {
    MULTIPLE_CHOICE,
    DROPDOWN,
    SHORT_ANSWER,
    NUMBER,
    DATE
}

data class Question(
    val id: Int,
    val question: String,
    val type: QuestionType,
    val options: List<String>? = null,  // Only used for MULTIPLE_CHOICE & DROPDOWN
    val isRequired: Boolean = true
)
