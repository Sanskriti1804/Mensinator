package com.mensinator.app.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mensinator.app.R
import com.mensinator.app.data.Symptom
import com.mensinator.app.ui.ResourceMapper
import com.mensinator.app.ui.theme.MensinatorTheme
import com.mensinator.app.ui.theme.appLRed
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentSet
import java.time.LocalDate

@Composable
fun EditSymptomsForDaysDialog(
    date: LocalDate,
    symptoms: PersistentSet<Symptom>,
    currentlyActiveSymptomIds: PersistentSet<Int>,
    onSave: (PersistentSet<Symptom>) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedSymptoms by remember {
        mutableStateOf(
            symptoms.filter { it.id in currentlyActiveSymptomIds }.toPersistentSet()
        )
    }

    AlertDialog(
        onDismissRequest = { onCancel() },
        containerColor = Color.White, // White background for the dialog
        confirmButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp) // Tight button spacing
            ) {
                Button(
                    onClick = { onSave(selectedSymptoms) },
                    colors = ButtonDefaults.buttonColors(containerColor = appLRed), // Red button
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.save_symptoms_button).uppercase(),
                        color = Color.White // White text in the button
                    )
                }
                Button(
                    onClick = { onCancel() },
                    colors = ButtonDefaults.buttonColors(containerColor = appLRed), // Red button
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel_button).uppercase(),
                        color = Color.White // White text in the button
                    )
                }
            }
        },
        modifier = modifier,
        dismissButton = {}, // Dismiss button already handled by confirmButton, so empty here
        title = {
            val dialogFont = FontFamily(Font(R.font.secfont)) // Closing bracket added here 😤✌️
            Text(
                text = stringResource(id = R.string.symptoms_dialog_title, date),
                style = TextStyle(
                    fontFamily = dialogFont, fontSize = 20.sp
                )
            )
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                symptoms.forEach { symptom ->
                    val symptomKey = ResourceMapper.getStringResourceId(symptom.name)
                    val symptomDisplayName =
                        symptomKey?.let { stringResource(id = it) } ?: symptom.name
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                val newSet = if (selectedSymptoms.contains(symptom)) {
                                    selectedSymptoms - symptom
                                } else {
                                    selectedSymptoms + symptom
                                }
                                selectedSymptoms = newSet.toPersistentSet()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedSymptoms.contains(symptom),
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(checkedColor = Color.Gray) // Gray checkbox when selected
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = symptomDisplayName)
                    }
                }
            }
        },
    )
}

@Preview
@Composable
private fun EditSymptomsForDaysDialog_OneDayPreview() {
    val symptoms = persistentSetOf(
        Symptom(1, "Light", 0, ""),
        Symptom(2, "Medium", 1, ""),
    )
    MensinatorTheme {
        EditSymptomsForDaysDialog(
            date = LocalDate.now(),
            symptoms = symptoms,
            currentlyActiveSymptomIds = persistentSetOf(2),
            onSave = {},
            onCancel = { },
        )
    }
}

@Preview
@Composable
private fun EditSymptomsForDaysDialog_MultipleDaysPreview() {
    val symptoms = persistentSetOf(
        Symptom(1, "Light", 0, ""),
        Symptom(2, "Medium", 1, ""),
    )
    MensinatorTheme {
        EditSymptomsForDaysDialog(
            date = LocalDate.now(),
            symptoms = symptoms,
            currentlyActiveSymptomIds = persistentSetOf(2),
            onSave = {},
            onCancel = { },
        )
    }
}
