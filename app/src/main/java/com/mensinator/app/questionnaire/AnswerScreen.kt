package com.mensinator.app.questionnaire

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun QuestionnaireAnswersScreen(
    answers: List<QuestionnaireAnswer>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(16.dp)
    ) {
        items(answers) { answer ->
            AnswerItem(answer)
            Divider(color = Color.LightGray, thickness = 1.dp)
        }
    }
}

@Composable
fun AnswersScreen(
    viewModel: QuestionnaireViewModel,
    modifier: Modifier = Modifier
) {
    val answers by viewModel.answers.collectAsState()
    val questions = remember { Constants.getQuestions() }

    LazyColumn(
        modifier = modifier.padding(16.dp)
    ) {
        items(QuestionnaireAnswer.fromMap(answers, questions)) { answer ->
            Column(
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Text(
                    text = answer.questionText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = answer.answer,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.DarkGray
                )
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
        }
    }
}
@Composable
fun AnswerItem(answer: QuestionnaireAnswer) {
    Column(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        Text(
            text = answer.questionText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = answer.answer,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.DarkGray
        )
    }
}