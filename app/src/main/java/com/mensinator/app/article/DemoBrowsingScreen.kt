package com.mensinator.app.article

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mensinator.app.ui.theme.MensinatorTheme
import com.mensinator.app.ui.theme.appDRed
import com.mensinator.app.ui.theme.appWhite

@Composable
fun ArticleBrowsingScreen(
    navController: NavController,
    headers: List<HeaderItem>,
    modifier: Modifier = Modifier,
    onCardClick: (String) -> Unit,
    headerBackgroundColor: Color = MaterialTheme.colorScheme.primary,
    cardBackgroundColor: Color = Color.White,
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    LazyColumn(modifier = modifier.background(Color.White)) {
        headers.forEach { header ->
            item {
                StickyHeader(
                    headerTitle = header.title,
                    backgroundColor = appWhite
                )
            }
            item {
                LazyRow(
                    contentPadding = contentPadding,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(header.cards) { card ->
                        ArticleCard(
                            card = card,
                            backgroundColor = cardBackgroundColor,
                            onClick = {
                                onCardClick(card.articleId)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleCard(
    card: CardItem,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(180.dp)
            .height(200.dp)
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = appDRed
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = card.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
        }
    }
}

@Composable
fun StickyHeader(
    headerTitle: String,
    backgroundColor: Color = Color.White  // Default to white
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .height(48.dp)
            .padding(start = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = headerTitle,
            color = Color.Black,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleCardPreview() {
    MensinatorTheme {
        Surface(modifier = Modifier.size(180.dp, 200.dp)) {
            ArticleCard(
                card = CardItem(
                    title = "Sample Article Title That Might Be Long",
                    articleId = "1"
                ),
                backgroundColor = Color.White,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StickyHeaderPreview() {
    MensinatorTheme {
        Surface {
            StickyHeader(
                headerTitle = "Category Header",
                backgroundColor = Color.White
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun ArticleBrowsingScreenPreview() {
    MensinatorTheme {
        val sampleHeaders = listOf(
            HeaderItem(
                title = "Health",
                cards = listOf(
                    CardItem("1", "Mental Health Tips"),
                    CardItem("2", "Physical Exercise Guide"),
                    CardItem("3", "Nutrition Advice"),
                    CardItem("4", "Sleep Improvement")
                )
            ),
            HeaderItem(
                title = "Relationships",
                cards = listOf(
                    CardItem("5", "Communication Skills"),
                    CardItem("6", "Building Trust"),
                    CardItem("7", "Conflict Resolution")
                )
            ),
            HeaderItem(
                title = "Career",
                cards = listOf(
                    CardItem("8", "Interview Tips"),
                    CardItem("9", "Networking Strategies"),
                    CardItem("10", "Work-Life Balance")
                )
            )
        )

        ArticleBrowsingScreen(
            navController = rememberNavController(),
            headers = sampleHeaders,
            onCardClick = {}
        )
    }
}

