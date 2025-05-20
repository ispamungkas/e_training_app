package com.maspam.etrain.training.presentation.training.event

import android.net.Uri
import com.maspam.etrain.training.data.dto.body.TrainingBody

sealed class FormTrainingEvent {
    data class NameChange(val name: String): FormTrainingEvent()
    data class DescriptionChange(val description: String): FormTrainingEvent()
    data class DatelineChange(val dateline: Long): FormTrainingEvent()
    data class AttendChange(val attend: Long): FormTrainingEvent()
    data class ImgChange(val img: Uri): FormTrainingEvent()

    data class Submit(val trainingBody: TrainingBody) : FormTrainingEvent()
    data class Update(val trainingBody: TrainingBody) : FormTrainingEvent()
}