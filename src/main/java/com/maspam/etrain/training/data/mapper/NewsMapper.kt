package com.maspam.etrain.training.data.mapper

import com.maspam.etrain.training.data.dto.Article
import com.maspam.etrain.training.domain.model.NewsModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun Article.toNewsModel(): NewsModel {
    return NewsModel(
        image = this.urlToImage,
        name = this.title,
        desc = this.content,
        author = this.author,
        publishDate = this.publishedAt.toEpochTime()
    )
}

fun String.toEpochTime(): Long? {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val date = sdf.parse(this)
    return date?.time?.div(1000)
}