package com.mensinator.app.questionnaire

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mensinator.app.ui.theme.appDRed
import com.mensinator.app.ui.theme.appGray

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

    Column(
        modifier = Modifier
            .padding(16.dp, top = 78.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress bar with centered width (not full width)
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f) // 90% of screen width
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedProgressBar(progress = progress)
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) // Add horizontal padding
            ) {
                // Question text - centered and bold
                Text(
                    text = question.question,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 24.dp),
                    textAlign = TextAlign.Center // Ensure text is centered
                )

                // Text field with red border
                var answer by remember { mutableStateOf(answers[question.id.toString()] ?: "") }
                OutlinedTextField(
                    value = answer,
                    onValueChange = {
                        answer = it
                        answers[question.id.toString()] = it
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f), // 90% of available width
                    shape = RoundedCornerShape(8.dp),
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Navigation buttons row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp), // Add horizontal padding
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentQuestionIndex > 0) {
                    NavigationButton(
                        onClick = { currentQuestionIndex-- },
                        icon = Icons.Default.ArrowBack,
                        containerColor = appGray,
                        modifier = Modifier.size(56.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.size(56.dp)) // Maintain spacing
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
                    containerColor = appDRed,
                    modifier = Modifier.size(56.dp)
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
            .height(14.dp)
            .clip(RoundedCornerShape(4.dp)),
        color = appDRed,
        trackColor = appGray,
    )
}

@Composable
fun NavigationButton(
    onClick: () -> Unit,
    icon: ImageVector,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape, // Make button circular
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        contentPadding = PaddingValues(0.dp) // Remove internal padding
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}