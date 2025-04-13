package com.mensinator.app.article

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mensinator.app.ui.theme.appWhite

@Composable
fun ArticleLayout(
    heading: String,
    content: String
) {
    val headingFont = FontFamily(
        Font(R.font.secfont) // use your actual file
    )
    val contentFont = FontFamily(
        Font(R.font.textfont) // use your actual file
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(appWhite)
            .padding(24.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Text(
                text = heading,
                fontFamily = headingFont,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = appDRed,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = content,
                fontFamily = headingFont,
                fontSize = 18.sp,
                lineHeight = 12.sp,
                color = appBlack,
//                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun ArticleScreen() {
    ArticleLayout(
        heading = ArticleOne.heading,
        content = ArticleOne.article
    )
}