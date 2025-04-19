package com.maspam.etrain.training.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseNewsAPI(
    val status: String,
    val totalResults: Long,
    val articles: List<Article>,
)

@Serializable
data class Article(
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String,
)