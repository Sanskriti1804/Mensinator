package com.mensinator.app.article

@Composable
fun StickyHeaderPage(headers: List<HeaderItem>, onCardClick: (String) -> Unit) {
    LazyColumn {
        headers.forEach { header ->
            stickyHeader {
                // Sticky Header Layout
                Text(
                    text = header.title,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            item {
                // Horizontal list of cards for each header
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    items(header.cards) { card ->
                        CardView(card, onCardClick)
                    }
                }
            }
        }
    }
}

@Composable
fun CardView(card: CardItem, onCardClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onCardClick(card.articleId) }
            .width(180.dp),
        elevation = 4.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(painter = painterResource(id = card.iconResId), contentDescription = card.title)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = card.title, style = MaterialTheme.typography.body2)
        }
    }
}
