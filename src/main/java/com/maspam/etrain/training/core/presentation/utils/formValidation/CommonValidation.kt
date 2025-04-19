package com.maspam.etrain.training.core.presentation.utils.formValidation

import android.content.Context
import com.maspam.etrain.R

class CommonValidation(
    private val context: Context,
    private val fieldNameValidation: String? = ""
) {

    fun execute(value: String): ValidationResult {
        if(value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.the_value_can_t_be_blank, fieldNameValidation)
            )
        }

        return ValidationResult(
            successful = true
        )
    }

}