package com.maspam.etrain.training.presentation.news.event

import android.net.Uri
import com.maspam.etrain.training.data.dto.body.NewsBody

sealed class FormNewsEvent {
    data class NameChange(val name: String): FormNewsEvent()
    data class DescChange(val desc: String): FormNewsEvent()
    data class AuthorChange(val author: String): FormNewsEvent()
    data class ImgChange(val img: Uri): FormNewsEvent()

    data class Submit(val newsBody: NewsBody) : FormNewsEvent()
    data class Update(val newsBody: NewsBody) : FormNewsEvent()
}