package com.mensinator.app.article

data class Article(
    val articleId: String,
    val heading: String,
    val content: String
)

data class CardItem(
    val articleId: String,   // Article ID as String for onClick
    val title: String
)

data class HeaderItem(
    val title: String,
    val cards: List<CardItem>
)

val articles = listOf(
    Article(
        articleId = "1",
        heading = ArticleOne.heading,
        content = ArticleOne.article
    ),
    Article(
        articleId = "2",
        heading = ArticleTwo.heading,
        content = ArticleTwo.article
    ),
    Article(
        articleId = "3",
        heading = ArticleThree.heading,
        content = ArticleThree.article
    ),
    Article(
        articleId = "4",
        heading = ArticleFour.heading,
        content = ArticleFour.article
    ),
    Article(
        articleId = "5",
        heading = ArticleFive.heading,
        content = ArticleFive.article
    ),
    Article(
        articleId = "6",
        heading = ArticleSix.heading,
        content = ArticleSix.article
    ),
    Article(
        articleId = "7",
        heading = ArticleSeven.heading,
        content = ArticleSeven.article
    ),
    Article(
        articleId = "8",
        heading = ArticleEight.heading,
        content = ArticleEight.article
    ),
    Article(
        articleId = "9",
        heading = ArticleNine.heading,
        content = ArticleNine.article
    ),
    Article(
        articleId = "10",
        heading = ArticleTen.heading,
        content = ArticleTen.article
    ),
    Article(
        articleId = "11",
        heading = ArticleEleven.heading,
        content = ArticleEleven.article
    ),
    Article(
        articleId = "12",
        heading = ArticleTwelve.heading,
        content = ArticleTwelve.article
    ),
    Article(
        articleId = "13",
        heading = ArticleOneThree.heading,
        content = ArticleOneThree.article
    ),
    Article(
        articleId = "14",
        heading = ArticleOneFour.heading,
        content = ArticleOneFour.article
    ),
    Article(
        articleId = "15",
        heading = ArticleOneFive.heading,
        content = ArticleOneFive.article
    ),
    Article(
        articleId = "16",
        heading = ArticleOneSix.heading,
        content = ArticleOneSix.article
    ),
    Article(
        articleId = "17",
        heading = ArticleOneSeven.heading,
        content = ArticleOneSeven.article
    ),
    Article(
        articleId = "18",
        heading = ArticleOneEight.heading,
        content = ArticleOneEight.article
    ),
    Article(
        articleId = "19",
        heading = ArticleOneNine.heading,
        content = ArticleOneNine.article
    ),
)


val headers = listOf(
    HeaderItem(
        title = "",
        cards = listOf(
            CardItem("1", "Article 1", R.drawable.ic_article), // Replace with your icon resource
            CardItem("4", ArticleFour.heading)
        )
    ),
    HeaderItem(
        title = "Header 2",
        cards = listOf(
            CardItem("3", "Article 3", R.drawable.ic_article) // Replace with your icon resource
        )
    ),

    // Add more headers and cards as needed
)

