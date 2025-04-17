package com.mensinator.app.questionnaire

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun QuestionnaireScreen(
    questions: List<Question>,
    onSubmit: (Map<String, String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val answers = remember { mutableStateMapOf<String, String>() }

    Column(modifier = Modifier.padding(16.dp, top = 78.dp)) {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]

            QuestionItemWithAnswerCapture(
                question = question,
                initialAnswer = answers[question.id.toString()] ?: "",
                onAnswerChanged = { newAnswer ->
                    answers[question.id.toString()] = newAnswer
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (currentQuestionIndex < questions.size - 1) {
                NextButton { currentQuestionIndex++ }
            } else {
                SubmitButton { onSubmit(answers) }
            }
        }
    }
}

@Composable
fun NextButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
    ) {
        Text(text = "Next", color = Color.White)
    }
}

@Composable
fun SubmitButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
    ) {
        Text(text = "Submit", color = Color.White)
    }
}
