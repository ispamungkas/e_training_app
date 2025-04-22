package com.maspam.etrain.training.data.mapper

import com.maspam.etrain.training.data.dto.PostTestDto
import com.maspam.etrain.training.domain.model.PostTestModel

fun PostTestDto.toPostTestModel(): PostTestModel {
    return PostTestModel(
        id = this.id,
        question = this.question,
        section = this.section,
        train = this.train,
        answers = this.answers
    )
}