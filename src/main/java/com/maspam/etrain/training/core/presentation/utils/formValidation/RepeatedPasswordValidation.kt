package com.maspam.etrain.training.core.presentation.utils.formValidation

import android.content.Context
import com.maspam.etrain.R

class RepeatedPasswordValidation(
    private val context: Context
) {

    fun execute(password: String, repeatedPassword: String): ValidationResult {
        if(repeatedPassword.isEmpty()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.the_value_can_t_be_blank, "Password")
            )
        }

        if(password != repeatedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.the_passwords_don_t_match)
            )
        }
        return ValidationResult(
            successful = true
        )
    }

}
