package com.maspam.etrain.training.presentation.training.state

import androidx.compose.runtime.Immutable
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.data.dto.body.TopicBody
import com.maspam.etrain.training.domain.model.TopicModel

@Immutable
data class ListTopicState(
    val isLoading: Boolean = false,
    val isRefresh: Boolean = false,
    val isSuccess: Boolean = false,
    val error: NetworkError? = null,
    val showModal: Boolean = false,
    val showConfirmation: Boolean = false,
    val topicId: Int? = 0,

    val data: List<TopicModel>? = null,
    val sectionId: Int? = 0,

    val selectedData: TopicBody? = null,
    val contentError: String? = "",

    val isPictureWasAdded: Boolean? = false
)