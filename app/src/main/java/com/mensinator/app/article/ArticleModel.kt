package com.mensinator.app.article

data class Article(
    val articleId: String,
    val heading: String,
    val content: String
)

data class CardItem(
    val articleId: String,   // Article ID as String for onClick
    val title: String,
) {
    val iconResId: Int = 0
}

data class HeaderItem(
    val title: String,
    val items: List<String>
)

val headerItems = listOf(
    HeaderItem(
        title = "Body & Physical Health",
        items = listOf(
            ArticleOne.heading,
            ArticleFour.heading,
            ArticleOneEight.heading,
            ArticleNine.heading,
            ArticleTen.heading,
            ArticleEight.heading,
            ArticleOneThree.heading
        )
    ),
    HeaderItem(
        title = "Mood, PMS & Mental Health",
        items = listOf(
            ArticleThree.heading,
            ArticleEleven.heading,
            ArticleOneFive.heading,
            ArticleOneSix.heading,
        )
    ),
    HeaderItem(
        title = "Food & Lifestyle",
        items = listOf(
            ArticleTwo.heading,
            ArticleEleven.heading,
            ArticleOneSeven.heading,

            )
    ),
    HeaderItem(
        title = "Sexual & Reproductive Health",
        items = listOf(
            ArticleTwelve.heading,
            ArticleFour.heading,
            ArticleEight.heading,
            ArticleOneThree.heading,
        )
    ),
    HeaderItem(
        title = "Contraception & Protection",
        items = listOf(
            ArticleSix.heading,
            ArticleSeven.heading,
            ArticleTwelve.heading,
        )
    ),
    HeaderItem(
        title = "Period Tracking & Awareness",
        items = listOf(
//            ArticleFour.ArticleTwenty.heading,
            ArticleTen.heading,
            ArticleNine.heading
        )
    ),
    HeaderItem(
        title = "Skin, Glow & Beauty",
        items = listOf(
            ArticleOne.heading,
            ArticleTwo.heading,
            ArticleEleven.heading
        )
    ),
    HeaderItem(
        title = "Hygiene & Products",
        items = listOf(
            ArticleOneNine.heading,
            ArticleEight.heading,
            ArticleOneSeven.heading,
        )
    ),
    HeaderItem(
        title = "Culture, Myths & Beliefs",
        items = listOf(
            ArticleOneFive.heading,
            ArticleOneSix.heading,

            )
    )

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

//
//val headers = listOf(
//    HeaderItem(
//        title = "1.Body & Physical Health\n",
//        cards = listOf(
//            CardItem("1", ArticleOne.heading),
//            CardItem("4", ArticleFour.heading),
//            CardItem("18", ArticleOneEight.heading),
//            CardItem("9", ArticleNine.heading),
//            CardItem("14", ArticleOneFour.heading),
//            CardItem("10", ArticleTen.heading),
//            CardItem("8", ArticleEight.heading),
//            CardItem("13", ArticleOneThree.heading),
//            CardItem("13", ArticleOneThree.heading)
//        )
//    ),
//    HeaderItem(
//        title = "2.Mood, PMS & Mental Health\n",
//        cards = listOf(
//            CardItem("3", ArticleThree.heading),
//            CardItem("11", ArticleEleven.heading),
//            CardItem("15", ArticleOneFive.heading),
//            CardItem("16", ArticleOneSix.heading)
//        )
//    ),
//
//    HeaderItem(
//        title = "3.Food & Lifestyle\n",
//        cards = listOf(
//            CardItem("2", ArticleTwo.heading),
//            CardItem("11", ArticleEleven.heading),
//            CardItem("17", ArticleOneSeven.heading)
//        )
//    ),
//
//    HeaderItem(
//        title = "4.Sexual & Reproductive Health\n",
//        cards = listOf(
//            CardItem("12", ArticleTwelve.heading),
//            CardItem("4", ArticleFour.heading),
//            CardItem("8", ArticleEight.heading),
//            CardItem("13", ArticleOneThree.heading),
//        )
//    ),
//
//    HeaderItem(
//        title = "5.Contraception & Protection\n",
//        cards = listOf(
//            CardItem("6", ArticleSix.heading),
//            CardItem("7", ArticleSeven.heading),
//            CardItem("12", ArticleTwelve.heading),
//        )
//    ),
//
//    HeaderItem(
//        title = "6.Period Tracking & Awareness\n",
//        cards = listOf(
//            CardItem("20", ArticleFour.ArticleTwenty.heading),
//            CardItem("10", ArticleTen.heading),
//            CardItem("9", ArticleNine.heading)
//        )
//    ),
//
//    HeaderItem(
//        title = "7.Skin, Glow & Beauty\n",
//        cards = listOf(
//            CardItem("1", ArticleOne.heading),
//            CardItem("2", ArticleTwo.heading),
//            CardItem("11", ArticleEleven.heading)
//        )
//    ),
//
//    HeaderItem(
//        title = "8.Hygiene & Products\n",
//        cards = listOf(
//            CardItem("19", ArticleOneNine.heading),
//            CardItem("8", ArticleEight.heading),
//            CardItem("17", ArticleOneSeven.heading)
//        )
//    ),
//
//    HeaderItem(
//        title = "9.Culture, Myths & Beliefs\n",
//        cards = listOf(
//            CardItem("15", ArticleOneFive.heading),
//            CardItem("16", ArticleOneSix.heading),
//        )
//    ),
//
//
//    // Add more headers and cards as needed
//)
//
