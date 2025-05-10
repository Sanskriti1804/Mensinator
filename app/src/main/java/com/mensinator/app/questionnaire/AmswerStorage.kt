package com.mensinator.app.questionnaire

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

// AnswerStorage.kt
class AnswerStorage(private val context: Context) {
    private val prefs = context.getSharedPreferences("user_answers", Context.MODE_PRIVATE)
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    suspend fun saveAnswers(answers: Map<Int, String>) {
        prefs.edit().apply {
            answers.forEach { (id, answer) ->
                putString("q_$id", answer)
            }
        }.apply()
    }

    fun loadAnswers(): Map<Int, String> {
        return Constants.getQuestions().associate { question ->
            question.id to (prefs.getString("q_${question.id}", "") ?: "")
        }
    }

    fun clearAnswers() {
        prefs.edit().clear().apply()
    }
}