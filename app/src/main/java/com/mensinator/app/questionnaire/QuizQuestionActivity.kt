package com.mensinator.app.questionnaire

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


class QuizQuestionsActivity : ComponentActivity() {

    private var mCurrentPosition: Int by mutableStateOf(1)
    private var mSelectedOptionPosition: Int by mutableStateOf(0)
    private var mUserName: String? = null
    private val mQuestionList: List<Question> = Constants.getQuestions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUserName = intent.getStringExtra(Constants.USER_NAME)

        setContent {
            QuizApp(
                currentPosition = mCurrentPosition,
                questionList = mQuestionList,
                selectedOptionPosition = mSelectedOptionPosition,
                onAnswerSelected = { selectedOption ->
                    mSelectedOptionPosition = selectedOption
                },
                onSubmitClicked = {
                    mCurrentPosition++
                    if (mCurrentPosition <= mQuestionList.size) {
                        mSelectedOptionPosition = 0
                    } else {
                        finish()
                    }
                }
            )
        }
    }
}

@Composable
fun QuizApp(
    currentPosition: Int,
    questionList: List<Question>,
    selectedOptionPosition: Int,
    onAnswerSelected: (Int) -> Unit,
    onSubmitClicked: () -> Unit
) {
    val question = questionList[currentPosition - 1]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Question $currentPosition/${questionList.size}",
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Image
        if (question.image != 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = question.image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = question.question,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column {
            question.options.forEachIndexed { index, option ->
                OptionButton(
                    text = option,
                    isSelected = selectedOptionPosition == index + 1,
                    onClick = { onAnswerSelected(index + 1) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onSubmitClicked) {
            Text(
                text = if (currentPosition == questionList.size) "Finish" else "Next Question"
            )
        }
    }
}

@Composable
fun OptionButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color.LightGray else Color.White
    val borderColor = if (isSelected) Color.Black else Color.Gray

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor)
    ) {
        Text(text = text, color = Color.Black)
    }
}
