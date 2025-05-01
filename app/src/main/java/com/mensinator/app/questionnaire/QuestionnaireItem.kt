package com.mensinator.app.questionnaire

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun QuestionItemWithAnswerCapture(
    question: Question,
    initialAnswer: String,
    onAnswerChanged: (String) -> Unit
) {
    val redColor = Color(0xFFFF5252) // Red color for selections

    Text(
        text = question.question,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 8.dp)
    )

    when (question.type) {
        QuestionType.SHORT_ANSWER, QuestionType.NUMBER -> {
            var answer by remember { mutableStateOf(initialAnswer) }
            OutlinedTextField(
                value = answer,
                onValueChange = {
                    answer = if (question.type == QuestionType.NUMBER)
                        it.filter { char -> char.isDigit() } else it
                    onAnswerChanged(answer)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Answer") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = redColor,
                    unfocusedBorderColor = redColor,
                    focusedLabelColor = redColor,
                    cursorColor = redColor
                )
            )
        }

        QuestionType.MULTIPLE_CHOICE -> {
            var selectedOption by remember { mutableStateOf(initialAnswer) }
            Column {
                question.options?.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedOption = option
                                onAnswerChanged(option)
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedOption == option,
                            onClick = {
                                selectedOption = option
                                onAnswerChanged(option)
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = redColor
                            )
                        )
                        Text(
                            text = option,
                            color = if (selectedOption == option) redColor else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        QuestionType.DROPDOWN -> {
            var expanded by remember { mutableStateOf(false) }
            var selectedText by remember { mutableStateOf(initialAnswer) }

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    label = { Text("Select") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = redColor,
                        unfocusedBorderColor = redColor,
                        focusedLabelColor = redColor
                    ),
                    trailingIcon = {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = redColor,
                            modifier = Modifier.clickable { expanded = !expanded }
                        )
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    question.options?.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option,
                                    color = if (selectedText == option) redColor
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                selectedText = option
                                expanded = false
                                onAnswerChanged(option)
                            }
                        )
                    }
                }
            }
        }

        QuestionType.DATE -> {
            val context = LocalContext.current
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            var selectedDate by remember { mutableStateOf(initialAnswer) }

            val datePickerDialog = DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    onAnswerChanged(selectedDate)
                },
                year, month, day
            )

            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Date") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = redColor,
                    unfocusedBorderColor = redColor,
                    focusedLabelColor = redColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() },
                placeholder = { Text("Click to select date") }
            )
        }
    }
}