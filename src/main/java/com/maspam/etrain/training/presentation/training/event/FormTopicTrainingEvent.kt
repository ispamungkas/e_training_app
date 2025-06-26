package com.maspam.etrain.training.presentation.training.event

import android.net.Uri
import com.maspam.etrain.training.data.dto.body.TopicBody

sealed class FormTopicTrainingEvent {
    data class NameChange(val name: String): FormTopicTrainingEvent()
    data class ContentChange(val content: String): FormTopicTrainingEvent()
    data class ImgChange(val img: Uri): FormTopicTrainingEvent()
    data class LinkVideoChange(val link: String): FormTopicTrainingEvent()

    data class Submit(val topicBody: TopicBody) : FormTopicTrainingEvent()
}