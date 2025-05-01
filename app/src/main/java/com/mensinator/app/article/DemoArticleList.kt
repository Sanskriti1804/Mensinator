package com.mensinator.app.article

@Composable
fun ArticleListScreen(
    articles: List<Article>,
    onArticleClick: (articleId: String) -> Unit // Click handler
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(appWhite)
            .padding(16.dp)
    ) {
        items(articles) { article ->
            Button(
                onClick = { onArticleClick(article.articleId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = appDRed, // Customize color
                    contentColor = appWhite
                )
            ) {
                Text(
                    text = article.heading, // Or use a shorter label like "Article ${article.articleId}"
                    fontSize = 16.sp
                )
            }
        }
    }
}