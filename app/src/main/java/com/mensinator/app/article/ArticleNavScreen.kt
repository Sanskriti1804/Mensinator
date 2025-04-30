package com.mensinator.app.article

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

//@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
//@Composable
//fun StickyHeaderPage(headers: List<HeaderItem>, onCardClick: (String) -> Unit) {
//    LazyColumn {
//        headers.forEach { header ->
//            stickyHeader {
//                // Sticky Header Layout
//                Text(
//                    text = header.title,
//                    style = MaterialTheme.typography.headlineSmall,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                )
//            }
//            item {
//                // Horizontal list of cards for each header
//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 8.dp)
//                ) {
//                    items(header.cards) { card ->
//                        CardView(card, onCardClick)
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
fun CardView(card: CardItem,
             onCardClick: (String) -> Unit,
             navController: NavHostController
             ) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                navController.navigate("articleDetail/${card.articleId}")
            }
            .width(180.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = card.iconResId),
                contentDescription = card.title
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = card.title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
