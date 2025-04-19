package com.maspam.etrain.training.core.presentation.utils

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.maspam.etrain.R
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.presentation.component.BadRequestDialog
import com.maspam.etrain.training.core.presentation.component.NoInternetDialog
import com.maspam.etrain.training.core.presentation.component.NotAcceptedDialog
import com.maspam.etrain.training.core.presentation.component.NotFoundDialog
import com.maspam.etrain.training.core.presentation.component.RequestTimeOut
import com.maspam.etrain.training.core.presentation.component.UnAuthorizedErrorDialog

fun NetworkError.errorToDisplay(context: Context): String {
    val res = when(this){
        NetworkError.REQUEST_TIMEOUT -> R.string.request_timeout
        NetworkError.UNAUTHORIZED -> R.string.unauthorized
        NetworkError.BAD_REQUEST -> R.string.bad_request
        NetworkError.NO_INTERNET -> R.string.no_internet
        NetworkError.NOT_ACCEPTED -> R.string.not_accepted
        NetworkError.SERVER_ERROR -> R.string.server_errror
        NetworkError.TOO_MANY_REQUEST -> R.string.to_many_request
        NetworkError.NOT_FOUND -> R.string.not_found
        NetworkError.UNKNOWN -> R.string.unknown
        NetworkError.SERIALIZATION -> R.string.serialization
    }
    return context.resources.getString(res)
}

@Composable
fun NetworkError.ToComposable(
    isLoading: Boolean? = false,
    navigateToLoginPage: () -> Unit,
    tryRequestAgain: () -> Unit,
    onDismiss: () -> Unit,
){
    when(this) {
        NetworkError.REQUEST_TIMEOUT -> {
            RequestTimeOut()
        }
        NetworkError.UNAUTHORIZED -> {
            UnAuthorizedErrorDialog(
                message = stringResource(R.string.session_was_ended),
                onResponse = navigateToLoginPage
            )
        }
        NetworkError.BAD_REQUEST -> {
            BadRequestDialog(
                onDismissDialog = onDismiss
            )
        }
        NetworkError.NO_INTERNET -> {
            NoInternetDialog(
                message = stringResource(R.string.no_internet_access),
                isLoading = isLoading ?: false,
                onClick = tryRequestAgain,
                onDismissDialog = onDismiss
            )
        }
        NetworkError.NOT_ACCEPTED -> {
            NotAcceptedDialog(
                onDismissDialog = onDismiss
            )
        }
        NetworkError.SERVER_ERROR -> {
            Spacer(modifier = Modifier)
        }
        NetworkError.TOO_MANY_REQUEST -> {
            Spacer(modifier = Modifier)
        }
        NetworkError.NOT_FOUND -> {
            NotFoundDialog()
        }
        NetworkError.SERIALIZATION -> {
            Spacer(modifier = Modifier)
        }
        NetworkError.UNKNOWN -> {
            Spacer(modifier = Modifier)
        }
    }

}