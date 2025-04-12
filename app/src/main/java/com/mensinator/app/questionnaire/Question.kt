package com.mensinator.app.questionnaire

data class Question(
    val id: Int,
    val question: String,
    val image: Int,
    val options: List<String>
)
