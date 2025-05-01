package com.mensinator.app.questionnaire

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun QuestionnaireScreen(
    questions: List<Question>,
    onSubmit: (Map<String, String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val answers = remember { mutableStateMapOf<String, String>() }

    val progress by animateFloatAsState(
        targetValue = if (questions.isEmpty()) 1f
        else (currentQuestionIndex + 1).toFloat() / questions.size,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "ProgressAnimation"
    )

    Column(modifier = Modifier.padding(16.dp, top = 78.dp)) {
        AnimatedProgressBar(progress = progress)
        Spacer(modifier = Modifier.height(16.dp))

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentQuestionIndex > 0) {
                    NavigationButton(
                        onClick = { currentQuestionIndex-- },
                        icon = Icons.Default.ArrowBack,
                        text = "Back",
                        containerColor = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                NavigationButton(
                    onClick = {
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                        } else {
                            onSubmit(answers)
                        }
                    },
                    icon = if (currentQuestionIndex < questions.size - 1)
                        Icons.Default.ArrowForward else Icons.Default.Check,
                    text = if (currentQuestionIndex < questions.size - 1) "Next" else "Submit",
                    containerColor = if (currentQuestionIndex < questions.size - 1)
                        Color.Blue else Color.Green,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun AnimatedProgressBar(progress: Float) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
        color = Color(0xFFFF5252),
        trackColor = Color(0xFFEEEEEE),
    )
}

@Composable
fun NavigationButton(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = text, tint = Color.White)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = text, color = Color.White)
        }
    }
}