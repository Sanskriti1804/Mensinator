package com.mensinator.app.article

data class Article(
    val articleId: String,
    val heading: String,
    val content: String
)

data class CardItem(
    val articleId: String,   // Article ID as String for onClick
    val title: String,
    val iconResId: Int = 0  // Made optional with default value
)

data class HeaderItem(
    val title: String,
    val cards: List<CardItem>  // Changed from items to cards to match usage
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
        title = "Body & Physical Health\n",
        cards = listOf(
            CardItem("1", ArticleOne.heading),
            CardItem("4", ArticleFour.heading),
            CardItem("18", ArticleOneEight.heading),
            CardItem("9", ArticleNine.heading),
            CardItem("14", ArticleOneFour.heading),
            CardItem("10", ArticleTen.heading),
            CardItem("8", ArticleEight.heading),
            CardItem("13", ArticleOneThree.heading)
        )
    ),
    HeaderItem(
        title = "Mood, PMS & Mental Health\n",
        cards = listOf(
            CardItem("3", ArticleThree.heading),
            CardItem("11", ArticleEleven.heading),
            CardItem("15", ArticleOneFive.heading),
            CardItem("16", ArticleOneSix.heading)
        )
    ),
    HeaderItem(
        title = "Food & Lifestyle\n",
        cards = listOf(
            CardItem("2", ArticleTwo.heading),
            CardItem("11", ArticleEleven.heading),
            CardItem("17", ArticleOneSeven.heading)
        )
    ),
    HeaderItem(
        title = "Sexual & Reproductive Health\n",
        cards = listOf(
            CardItem("12", ArticleTwelve.heading),
            CardItem("4", ArticleFour.heading),
            CardItem("8", ArticleEight.heading),
            CardItem("13", ArticleOneThree.heading)
        )
    ),
    HeaderItem(
        title = "Contraception & Protection\n",
        cards = listOf(
            CardItem("6", ArticleSix.heading),
            CardItem("7", ArticleSeven.heading),
            CardItem("12", ArticleTwelve.heading)
        )
    ),
    HeaderItem(
        title = "Period Tracking & Awareness\n",
        cards = listOf(
            CardItem("20", ArticleFour.ArticleTwenty.heading),  // Fixed reference
            CardItem("10", ArticleTen.heading),
            CardItem("9", ArticleNine.heading)
        )
    ),
    HeaderItem(
        title = "Skin, Glow & Beauty\n",
        cards = listOf(
            CardItem("1", ArticleOne.heading),
            CardItem("2", ArticleTwo.heading),
            CardItem("11", ArticleEleven.heading)
        )
    ),
    HeaderItem(
        title = "Hygiene & Products\n",
        cards = listOf(
            CardItem("19", ArticleOneNine.heading),
            CardItem("8", ArticleEight.heading),
            CardItem("17", ArticleOneSeven.heading)
        )
    ),
    HeaderItem(
        title = "Culture, Myths & Beliefs\n",
        cards = listOf(
            CardItem("15", ArticleOneFive.heading),
            CardItem("16", ArticleOneSix.heading)
        )
    )
)