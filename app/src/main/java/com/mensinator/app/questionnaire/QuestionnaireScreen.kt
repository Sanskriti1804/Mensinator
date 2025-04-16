package com.mensinator.app.questionnaire

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import java.lang.reflect.Modifier
import java.nio.file.WatchEvent

@Composable
fun QuestionnaireScreen(
    questions: List<Question>,
    onSubmit: (Map<String, String>) -> Unit // Map of questionId to answer
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val answers = remember { mutableStateMapOf<String, String>() }

    Column(modifier = Modifier.padding(16.dp)) {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]

            // QuestionItemWithAnswerCapture should render based on the question type
            QuestionItemWithAnswerCapture(
                question = question,
                initialAnswer = answers[question.id.toString()] ?: "",
                onAnswerChanged = { newAnswer ->
                    answers[question.id.toString()] = newAnswer
                }
            )

            // Next or Submit button logic
            if (currentQuestionIndex < questions.size - 1) {
                NextButton {
                    currentQuestionIndex++
                }
            } else {
                SubmitButton {
                    onSubmit(answers)
                }
            }
        }
    }
}

@Composable
fun NextButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
    ) {
        Text(text = "Next", color = Color.White)
    }
}

@Composable
fun SubmitButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
    ) {
        Text(text = "Submit", color = Color.White)
    }
}

@Composable
fun QuestionItemWithAnswerCapture(
    question: Question,
    initialAnswer: String,
    onAnswerChanged: (String) -> Unit
) {
    Column(modifier = WatchEvent.Modifier.fillMaxWidth()) {
        Text(text = question.question, style = MaterialTheme.typography.h6)
        Spacer(modifier = WatchEvent.Modifier.height(8.dp))

        when (question.type) {
            QuestionType.SHORT_ANSWER -> {
                TextField(
                    value = initialAnswer,
                    onValueChange = onAnswerChanged,
                    modifier = WatchEvent.Modifier.fillMaxWidth(),
                    label = { Text("Your Answer") }
                )
            }

            QuestionType.MULTIPLE_CHOICE -> {
                question.options?.forEach { option ->
                    Row {
                        RadioButton(
                            selected = initialAnswer == option,
                            onClick = { onAnswerChanged(option) }
                        )
                        Text(text = option)
                    }
                }
            }

            +

            QuestionType.DATE -> {
                // Handle date picker (use a DatePickerDialog or any other date input)
                TextField(
                    value = initialAnswer,
                    onValueChange = onAnswerChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Select Date") }
                )
            }

            else -> {
                TextField(
                    value = initialAnswer,
                    onValueChange = onAnswerChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Your Answer") }
                )
            }
        }
    }
}
