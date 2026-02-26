package com.mensinator.app.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mensinator.app.R
import com.mensinator.app.ui.theme.MensinatorTheme


@Composable
fun FaqDialog(
    onDismissRequest: () -> Unit, // Callback to handle the close action
    modifier: Modifier = Modifier
) {
    val ExheadingFont = FontFamily(
        Font(R.font.secfont) // Your custom font
    )
    AlertDialog(
        onDismissRequest = onDismissRequest,  // Call the dismiss callback when dialog is dismissed
        confirmButton = {
            Button(
                onClick = onDismissRequest,
                colors = ButtonDefaults.buttonColors(
                    containerColor = com.mensinator.app.ui.theme.appDRed,
                    contentColor = Color.White
                )
                // Call the dismiss callback when the button is clicked
            ) {
                Text(stringResource(id = R.string.close_button))
            }
        },
        modifier = modifier.fillMaxWidth(),
        title = {
            Text(
                text = stringResource(id = R.string.about_app),
                style = TextStyle(
                    fontFamily = ExheadingFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 38.sp
                )

            )
        },
        text = {
            FAQDialogContent()
        },
    )
}

@Composable
private fun FAQDialogContent() {
    Column(
        modifier = Modifier
            .padding(16.dp)  // Padding around the text content
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())  // Add vertical scrolling capability
    ) {
        // User Manual Header
        Text(
            text = stringResource(id = R.string.user_manual_header),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = com.mensinator.app.ui.theme.appDRed
        )
        Spacer(modifier = Modifier.height(8.dp)) // Space between sections

        // How to Use
        Text(
            text = stringResource(id = R.string.how_to_use),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = stringResource(id = R.string.select_dates),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(id = R.string.add_or_remove_dates),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(id = R.string.symptoms),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(id = R.string.ovulation),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(id = R.string.statistics),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(id = R.string.import_export),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp)) // Space between sections
        Text(
            text = stringResource(id = R.string.calculations),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(id = R.string.features_coming_soon),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = stringResource(id = R.string.upcoming_features),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp)) // Space between sections

        // Disclaimer Header
        Text(
            text = stringResource(id = R.string.disclaimer_header),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = stringResource(id = R.string.disclaimer),
            style = MaterialTheme.typography.bodyMedium
        )

    }
}

@Preview
@Composable
private fun FAQDialogPreview() {
    MensinatorTheme {
        FaqDialog(onDismissRequest = {})
    }
}