package com.mensinator.app.questionnaire

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionnaireViewModel(private val storage: AnswerStorage) : ViewModel() {
    private val _answers = mutableStateMapOf<Int, String>()
    private val _answersFlow = MutableStateFlow(_answers.toMap())
    val answers: StateFlow<Map<Int, String>> = _answersFlow.asStateFlow()

    init {
        viewModelScope.launch {
            _answers.putAll(storage.loadAnswers())
            _answersFlow.value = _answers.toMap()
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