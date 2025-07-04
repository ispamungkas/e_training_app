package com.maspam.etrain.training.data.mapper

import com.maspam.etrain.training.data.dto.SectionDto
import com.maspam.etrain.training.data.dto.TopicDto
import com.maspam.etrain.training.data.dto.TrainingDto
import com.maspam.etrain.training.domain.model.SectionModel
import com.maspam.etrain.training.domain.model.TopicModel
import com.maspam.etrain.training.domain.model.TrainingModel

fun TrainingDto.toTrainingModel(): TrainingModel {
    return TrainingModel(
        id = this.id,
        image = this.img,
        name = this.name,
        typeTraining = this.typeTrain,
        typeTrainingCategory = this.typeTrainAc,
        isOpen = this.isOpen,
        desc = this.desc,
        attend = this.attend,
        totalTaken = this.enrolls?.size,
        due = this.dateline,
        totalJp = this.totalJp,
        link = this.link,
        isPublish = this.isPublish,
        location = this.location,
        sections = this.sections?.map {
            it.toSectionModel()
        },
        postTest = this.postTest?.map {
            it.toPostTestModel()
        },
        createdAt = this.createdAt
    )
}

fun SectionDto.toSectionModel(): SectionModel {
    return SectionModel(
        id = this.id,
        name = this.name,
        jp = this.jp,
        status = this.status,
        trainId = this.trainId,
        topics = this.topics?.map {
            it.toTopicModel()
        }
    )
}

fun TopicDto.toTopicModel(): TopicModel {
    return TopicModel(
        id = this.id,
        name = this.name,
        content = this.content,
        sectionId = this.sectionId,
        topicImage = this.img,
        linkVideo = this.linkVideo
    )
}