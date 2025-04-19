package com.maspam.etrain.training.core.presentation.utils.formValidation

import android.content.Context
import android.util.Patterns
import com.maspam.etrain.R

class EmailValidation(private val context: Context) {

    fun execute(email: String): ValidationResult {
        if(email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.the_value_can_t_be_blank, "Email")
            )
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.that_s_not_a_valid_value, "Email")
            )
        }
        return ValidationResult(
            successful = true
        )
    }

}