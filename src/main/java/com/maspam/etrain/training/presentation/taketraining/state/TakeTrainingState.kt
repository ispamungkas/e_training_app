package com.maspam.etrain.training.presentation.taketraining.state

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.EnrollModel
import com.maspam.etrain.training.domain.model.PostTestModel
import com.maspam.etrain.training.domain.model.TopicModel

@Immutable
data class TakeTrainingState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isRefresh: Boolean = false,
    val error: NetworkError? = null,
    var data: EnrollModel? = null,
    val sectionSelection: Int = 0,
    val topicSelection: Int = 0,
    val isCheck: Boolean = true,
    val isLast: Boolean = true,
    val selectedTopic: TopicModel? = data?.trainingDetail?.sections?.get(0)?.topics?.get(0),

    val lastSection: Int = 0,
    val lastTopic: Int = 0,

    /**
     * for submit post test
     */

    val inputFailure: Boolean = false,
    val postTest: List<PostTestModel>? = emptyList(),
    var ansOverall: MutableList<MutableList<String>> = mutableListOf(),
    val selectedAnswer: String = "",

    /**
     * for submit karyanyata
     */
    val file: Uri? = null

)