package com.maspam.etrain.training.presentation.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.CustomButtonField
import com.maspam.etrain.training.core.presentation.component.CustomNumberTextField
import com.maspam.etrain.training.core.presentation.component.CustomTextField
import com.maspam.etrain.training.core.presentation.component.LoadingScreen
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.presentation.authentication.component.Head
import com.maspam.etrain.training.presentation.authentication.event.AuthenticationFormEvent
import com.maspam.etrain.training.presentation.authentication.viewmodel.AuthenticationViewModel

@Composable
fun RegisterPage(
    authenticationViewModel: AuthenticationViewModel,
    onRegisterSuccess: () -> Unit,
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {

    val state by authenticationViewModel.state.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    eventListener(event = authenticationViewModel.event) { event ->
        when (event) {
            is AuthenticationViewModel.AuthenticationEvent.Error -> authenticationViewModel.setError(
                event.e
            )

            AuthenticationViewModel.AuthenticationEvent.Success -> authenticationViewModel.setSuccess(
                value = true
            )
        }
    }

    // Show error if had
    state.errorResult?.ToComposable(
        navigateToLoginPage = navigateToLogin,
        tryRequestAgain = {
            authenticationViewModel.apply {
                setError(e = null)
                submitRegister()
            }
        },
        onDismiss = { authenticationViewModel.setError(e = null) }
    )

    if (state.isSuccess == true) {
        SuccessDialog(
            message = stringResource(R.string.register_success_word),
            onDismissDialog = {
                authenticationViewModel.setSuccess(value = false)
                onRegisterSuccess()
            }
        )

    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column {
            Head(section = stringResource(R.string.new_account), height = 150.dp)
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Lucide.Info, contentDescription = "Information")
                Text(
                    text = stringResource(R.string.new_account_information),
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light),
                )
            }
            Divider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))
            CustomNumberTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                label = stringResource(R.string.input_nip),
                errorMessage = state.nipErrorMessage
            ) {
                authenticationViewModel.onEvent(event = AuthenticationFormEvent.NipChanged(it))
            }
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                label = stringResource(R.string.input_name),
                errorMessage = state.nameErrorMessage
            ) {
                authenticationViewModel.onEvent(event = AuthenticationFormEvent.NameChanged(it))
            }
            Row(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.isHeadCheck ?: false,
                    onCheckedChange = {
                        authenticationViewModel.onEvent(
                            event = AuthenticationFormEvent.IsHeadChange(
                                it
                            )
                        )
                    }
                )
                Text(
                    text = stringResource(R.string.create_account_for_teacher),
                    style = MaterialTheme.typography.labelMedium
                )

            }
            CustomButtonField(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                buttonName = stringResource(R.string.submit),
                buttonColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    authenticationViewModel.onEvent(event = AuthenticationFormEvent.submitRegister)
                }
            )
        }
        Text(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 30.dp),
            text = stringResource(R.string.copyright_maspam),
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light)
        )

        if (state.isLoading == true) {
            keyboardController?.hide()
            LoadingScreen()
        }
    }
}

