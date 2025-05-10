package com.mensinator.app.questionnaire

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.mensinator.app.ui.theme.appDRed
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnswersScreen(
    viewModel: QuestionnaireViewModel,
    modifier: Modifier = Modifier
) {
    val answers by viewModel.answers.collectAsState()
    val questions = remember { Constants.getQuestions() }
    val questionnaireAnswers = remember(answers) {
        QuestionnaireAnswer.fromMap(answers, questions)
    }
    val listState = rememberLazyListState()

    Box(modifier = modifier
        .fillMaxSize()
        .background(Color.White)) {
        // Navigation Rail
        NavigationRail(
            itemCount = questionnaireAnswers.size,
            currentIndex = getCurrentVisibleIndex(listState),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
                .zIndex(1f)
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 40.dp) // Make space for navigation rail
        ) {
            // Answers List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 15.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(questionnaireAnswers) { index, answer ->
                    AnswerCard(
                        answer = answer,
                        onAnswerUpdated = { newAnswer ->
                            viewModel.updateAnswer(answer.questionId, newAnswer)
                        },
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }
        }
    }
}

@Composable
private fun AnswerCard(
    answer: QuestionnaireAnswer,
    onAnswerUpdated: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedAnswer by remember(answer.answer) {
        mutableStateOf(answer.answer)
    }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Track if card is in "selected" state (like a dialog)
    var isSelected by remember { mutableStateOf(false) }

    // Colors for different states
    val cardBackgroundColor by animateColorAsState(
        targetValue = when {
            isSelected -> appDRed // Selected state
            isEditing -> appDRed  // Editing state
            else -> Color.White   // Normal state
        },
        animationSpec = tween(durationMillis = 200)
    )

    val textColor by animateColorAsState(
        targetValue = when {
            isSelected || isEditing -> Color.White
            else -> MaterialTheme.colorScheme.onSurface
        },
        animationSpec = tween(durationMillis = 200)
    )

    // Click listener for the background to deselect
    val clickModifier = if (isSelected || isEditing) {
        Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    isSelected = false
                    isEditing = false
                    focusManager.clearFocus()
                }
            )
    } else {
        Modifier
    }

    Box(
        modifier = clickModifier,
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (isSelected || isEditing) 8.dp else 4.dp,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        isSelected = true
                        isEditing = true
                    }
                ),
            colors = CardDefaults.cardColors(
                containerColor = cardBackgroundColor
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Question Text
                Text(
                    text = answer.questionText,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = textColor,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (isEditing) {
                    // Editable Text Field
                    BasicTextField(
                        value = editedAnswer,
                        onValueChange = { editedAnswer = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                isEditing = false
                                isSelected = false
                                onAnswerUpdated(editedAnswer)
                                focusManager.clearFocus()
                            }
                        ),
                        cursorBrush = SolidColor(Color.White)
                    )

                    LaunchedEffect(isEditing) {
                        if (isEditing) {
                            delay(100)
                            focusRequester.requestFocus()
                        }
                    }
                } else {
                    // Static Answer Text
                    Text(
                        text = answer.answer,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        ),
                        color = textColor.copy(alpha = 0.8f),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
@Composable
private fun NavigationRail(
    itemCount: Int,
    currentIndex: Int,
    modifier: Modifier = Modifier,
    dotSize: Dp = 12.dp,
    lineWidth: Dp = 2.dp,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = Color.LightGray.copy(alpha = 0.6f)
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Vertical line
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(lineWidth)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            inactiveColor,
                            inactiveColor,
                            Color.Transparent
                        )
                    )
                )
        )

        // Dots positioned absolutely along the line
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(dotSize)
                .padding(vertical = dotSize)
        ) {
            repeat(itemCount) { index ->
                val isActive = index == currentIndex
                val color = if (isActive) activeColor else inactiveColor

                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = calculateDotPosition(index, itemCount))
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (isActive) 0.dp else 1.dp,
                            color = inactiveColor,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

private fun calculateDotPosition(index: Int, total: Int): Dp {
    // Calculate position as percentage (0-100) then convert to dp in the parent
    val percentage = if (total <= 1) 0.5f else index.toFloat() / (total - 1)
    return (percentage * 100).dp - 6.dp // Adjust for dot size
}

private fun getCurrentVisibleIndex(listState: LazyListState): Int {
    return listState.layoutInfo.visibleItemsInfo
        .firstOrNull { it.index == listState.firstVisibleItemIndex }
        ?.index ?: 0
}

@Composable
private fun UserInfoHeader(
    userName: String,
    userInfo: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = userInfo,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
    }
}