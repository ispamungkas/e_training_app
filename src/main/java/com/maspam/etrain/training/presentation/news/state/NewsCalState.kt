package com.maspam.etrain.training.presentation.news.state

import android.net.Uri
import com.maspam.etrain.training.core.domain.utils.NetworkError

data class NewsCalState(
    val isLoading: Boolean? = false,
    val isRefresh: Boolean = false,
    val isSuccess: Boolean = false,
    val error: NetworkError? = null,
    val userConfirmation: Boolean = false,

    /**
     * Form input
     */
    val name: String = "",
    val desc: String = "",
    val publishDate: Long = 0L,
    val author: String = "",
    val img: Uri = Uri.parse(""),

    val nameError: String = "",
    val descError: String = "",
    val publishDateError: String = "",
    val authorError: String = "",
    val imgError: String = ""
)
