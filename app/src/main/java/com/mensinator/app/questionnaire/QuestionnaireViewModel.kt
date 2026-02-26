package com.mensinator.app.questionnaire

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class QuestionnaireViewModel(
    private val storage: AnswerStorage,
    private val loadFromStorage: Boolean = true
) : ViewModel() {
    private val _answers = mutableStateMapOf<Int, String>()
    private val _answersFlow = MutableStateFlow(_answers.toMap())
    open val answers: StateFlow<Map<Int, String>> = _answersFlow.asStateFlow()

    init {
        if (loadFromStorage) {
            viewModelScope.launch {
                _answers.putAll(storage.loadAnswers())
                _answersFlow.value = _answers.toMap()
            }
        }
    }

    fun updateAnswer(questionId: Int, answer: String) {
        _answers[questionId] = answer
        _answersFlow.value = _answers.toMap()
        viewModelScope.launch {
            storage.saveAnswers(_answers)
        }
    }

    fun clearAnswers() {
        _answers.clear()
        _answersFlow.value = _answers.toMap()
        viewModelScope.launch {
            storage.clearAnswers()
        }
    }
}