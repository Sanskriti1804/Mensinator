package com.mensinator.app.article

data class CardItem(
    val title: String,
    val iconResId: Int,
    val articleId: String // This could be a link or an ID to navigate to the article
)

data class HeaderItem(
    val title: String,
    val cards: List<CardItem>
)
