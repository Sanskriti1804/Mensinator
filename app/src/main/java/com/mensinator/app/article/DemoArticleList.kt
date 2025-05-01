import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mensinator.app.article.Article
import com.mensinator.app.ui.theme.appDRed
import com.mensinator.app.ui.theme.appWhite

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