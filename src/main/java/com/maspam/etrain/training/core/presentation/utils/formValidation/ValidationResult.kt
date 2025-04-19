package com.maspam.etrain.training.core.presentation.utils.formValidation

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = "",
)
