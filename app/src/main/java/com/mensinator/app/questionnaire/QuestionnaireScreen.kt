package com.mensinator.app.questionnaire

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mensinator.app.R
import com.mensinator.app.ui.theme.appDRed
import com.mensinator.app.ui.theme.appGray

// Reusable Components
@Composable
private fun QuestionnaireProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier
            .height(14.dp)
            .clip(RoundedCornerShape(4.dp)),
        color = appDRed,
        trackColor = appGray,
    )
}

@Composable
private fun QuestionnaireHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Replace with your actual logo component
        Image(
            painter = painterResource(id = R.drawable.rlogo),
            contentDescription = "App Icon",
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 56.dp)
        )
        Text(
            text = "Let’s get to know your cycle – enter your period and health info below.",
            color = appDRed,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun QuestionnaireQuestionText(
    question: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = question,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun QuestionnaireAnswerField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = appDRed,
            unfocusedBorderColor = appGray
        )
    )
}

@Composable
private fun NavigationButton(
    onClick: () -> Unit,
    icon: ImageVector,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun AnimatedProgressBar(progress: Float) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth()
            .height(14.dp)
            .padding(1.dp)
            .clip(RoundedCornerShape(4.dp)),
        color = appDRed,
        trackColor = appGray,
    )
}

@Composable
private fun QuestionnaireNavigationButtons(
    showBackButton: Boolean,
    isLastQuestion: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        if (showBackButton) {
            NavigationButton(
                onClick = onBack,
                icon = Icons.Default.ArrowBack,
                containerColor = appGray,
                modifier = Modifier.size(64.dp)
            )
        } else {
            Spacer(modifier = Modifier.size(64.dp))
        }
        Spacer(modifier = Modifier.weight(1f))

        NavigationButton(
            onClick = onNext,
            icon = if (isLastQuestion) Icons.Default.Check else Icons.Default.ArrowForward,
            containerColor = appDRed,
            modifier = Modifier.size(64.dp)
        )
    }
}

@Composable
fun QuestionnaireScreen(
    viewModel: QuestionnaireViewModel,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = remember { Constants.getQuestions() }
    val answers by viewModel.answers.collectAsState()
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val progress by animateFloatAsState(
        targetValue = if (questions.isEmpty()) 1f
        else (currentQuestionIndex + 1).toFloat() / questions.size,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "ProgressAnimation"
    )

    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f) // 90% of screen width
                    .padding(horizontal = 20.dp, vertical = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedProgressBar(progress = progress)
            }

            // Modified logo section to be centered in upper half
            Box(
                modifier = Modifier
                    .weight(1f) // Takes half of the screen space
                    .fillMaxWidth()
                    .padding(vertical = 16.dp), // Added padding for better spacing
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .size(150.dp) // Fixed size as requested
                        .padding(16.dp) // Added padding around the logo
                )
            }
            // Question and input fields in the lower part
            if (questions.isNotEmpty() && currentQuestionIndex < questions.size) {
                val question = questions[currentQuestionIndex]

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header Text
                    Text(
                        text = "Let's get to know your cycle",
                        color = appDRed,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center
                    )

                    // Question Text
                    Text(
                        text = question.question,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Answer Field
                    OutlinedTextField(
                        value = answers[question.id] ?: "",
                        onValueChange = { answer ->
                            viewModel.updateAnswer(question.id, answer)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = appDRed,
                            unfocusedBorderColor = appGray
                        )
                    )

                    // Navigation Buttons
                    QuestionnaireNavigationButtons(
                        showBackButton = currentQuestionIndex > 0,
                        isLastQuestion = currentQuestionIndex == questions.size - 1,
                        onBack = { currentQuestionIndex-- },
                        onNext = {
                            if (currentQuestionIndex < questions.size - 1) {
                                currentQuestionIndex++
                            } else {
                                onComplete()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
