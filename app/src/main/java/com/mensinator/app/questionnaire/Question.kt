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
    val options: List<String>? = null,
    val isRequired: Boolean = true
)
