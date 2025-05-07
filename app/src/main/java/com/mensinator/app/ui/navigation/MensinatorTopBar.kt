package com.mensinator.app.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mensinator.app.R
import com.mensinator.app.ui.theme.MensinatorTheme
import com.mensinator.app.ui.theme.appDRed

@Composable
fun MensinatorTopBar(
    @StringRes titleStringId: Int,
    onTitleClick: (() -> Unit)? = null,
    textColor: androidx.compose.ui.graphics.Color = appDRed, // Changed to appDRed
    textStyle: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.textfont)),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    backgroundColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.background
) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 22.dp, vertical = 12.dp)
                .fillMaxWidth()
        ) {
            val modifier = onTitleClick?.let {
                Modifier
                    .clip(MaterialTheme.shapes.small)
                    .clickable { it() }
            } ?: Modifier

            Text(
                text = stringResource(titleStringId),
                modifier = modifier,
                style = textStyle,
                color = textColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private class ScreenTitleProvider : PreviewParameterProvider<Int> {
    override val values: Sequence<Int>
        get() = Screen.entries.map { it.titleRes }.asSequence()
}

@Preview(showBackground = true)
@Composable
private fun MensinatorTopBarPreview(
    @PreviewParameter(ScreenTitleProvider::class) stringId: Int,
) {
    MensinatorTheme {
        MensinatorTopBar(
            stringId,
            textColor = appDRed, // Ensure appDRed is used in preview
            backgroundColor = Color.White // Optional: Set background for better preview visibility
        )
    }
}