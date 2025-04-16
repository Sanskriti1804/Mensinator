package com.mensinator.app.questionnaire

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun QuestionItemWithAnswerCapture(
    question: Question,
    initialAnswer: String,
    onAnswerChanged: (String) -> Unit
) {
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
                    answer =
                        if (question.type == QuestionType.NUMBER) it.filter { char -> char.isDigit() } else it
                    onAnswerChanged(answer)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Answer") }
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
                            }
                        )
                        Text(text = option)
                    }
                }
            }
        }

        QuestionType.DROPDOWN -> {
            var expanded by remember { mutableStateOf(false) }
            var selectedText by remember { mutableStateOf(initialAnswer) }

            Box {
                OutlinedTextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Select") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.clickable { expanded = !expanded }
                        )
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    question.options?.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(text = option) },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() }
            )
        }
    }
}
