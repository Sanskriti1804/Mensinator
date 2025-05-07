package com.mensinator.app.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mensinator.app.R
import com.mensinator.app.ui.theme.MensinatorTheme
import com.mensinator.app.ui.theme.appDRed

@Composable
fun NotificationDialog(
    messageText: String,
    onDismissRequest: () -> Unit,
    onSave: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var newMessageText by remember { mutableStateOf(messageText) }
    val headingFont = FontFamily(
        Font(R.font.secfont) // Using the same custom font as Export/Import dialogs
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        containerColor = Color.White,
        title = {
            Text(
                text = stringResource(R.string.period_notification_title),
                style = TextStyle(
                    fontFamily = headingFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 26.sp
                )
            )
        },
        text = {
            TextField(
                value = newMessageText,
                onValueChange = { newMessageText = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(newMessageText)
                    onDismissRequest()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = appDRed,
                    contentColor = Color.White
                )
            ) {
                Text(text = stringResource(id = R.string.save_button))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest,
                colors = ButtonDefaults.buttonColors(
                    containerColor = appDRed,
                    contentColor = Color.White
                )
            ) {
                Text(text = stringResource(id = R.string.cancel_button))
            }
        }
    )
}

@Preview
@Composable
private fun NotificationDialogPreview() {
    MensinatorTheme {
        NotificationDialog(
            messageText = "Example message",
            onDismissRequest = {},
            onSave = {},
        )
    }
}