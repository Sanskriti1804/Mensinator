package com.mensinator.app.article

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mensinator.app.ui.theme.appBlack
import com.mensinator.app.ui.theme.appLRed
import com.mensinator.app.ui.theme.appWhite

@Composable
fun ArticleBrowsingScreen(
    headers: List<HeaderItem>,
    onCardClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    headerBackgroundColor: Color = appWhite,
    cardBackgroundColor: Color = appLRed, // Changed to appDRed
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    LazyColumn(modifier = modifier) {
        headers.forEach { header ->
            item {
                StickyHeader(
                    headerTitle = header.title,
                    backgroundColor = headerBackgroundColor
                )
            }
            item {
                LazyRow(
                    contentPadding = contentPadding,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(header.items) {cardItem ->
                        Card(
                            modifier = Modifier
                                .clickable { onCardClick(cardItem.articleId) }
                                .padding(8.dp)
                                .width(180.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Text(
                                text = cardItem.title,
                                modifier = Modifier.padding(16.dp),
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
//                        item ->
//                        RectangularCard(
//                            item = item,
//                            backgroundColor = cardBackgroundColor,
//                            onClick = { onCardClick(item) }
//                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StickyHeader(
    headerTitle: String,
    backgroundColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Top divider
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Text(
                text = headerTitle.uppercase(),
                color = appBlack,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge
            )
        }

        // Bottom divider
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
fun RectangularCard(
    item: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .width(150.dp)  // Reduced width
            .height(200.dp)  // Increased height
            .padding(4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item,
                color = appWhite, // Changed to appWhite
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}