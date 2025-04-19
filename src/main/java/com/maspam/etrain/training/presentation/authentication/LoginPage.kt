package com.maspam.etrain.training.presentation.authentication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.CustomButtonField
import com.maspam.etrain.training.core.presentation.component.CustomNumberTextField
import com.maspam.etrain.training.core.presentation.component.CustomTextPasswordField
import com.maspam.etrain.training.core.presentation.component.LoadingScreen
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.domain.model.UserModel
import com.maspam.etrain.training.presentation.authentication.component.Head
import com.maspam.etrain.training.presentation.authentication.event.AuthenticationFormEvent
import com.maspam.etrain.training.presentation.authentication.viewmodel.AuthenticationViewModel

@Composable
fun LoginPage(
    authenticationViewModel: AuthenticationViewModel,
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit,
    onLoginSuccess: (UserModel) -> Unit,
    onNavigateToForgotPasswordScreen: () -> Unit
) {
    
    val state by authenticationViewModel.state.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    eventListener(event = authenticationViewModel.event) { event ->
        when(event) {
            is AuthenticationViewModel.AuthenticationEvent.Error -> authenticationViewModel.setError(event.e)
            AuthenticationViewModel.AuthenticationEvent.Success -> authenticationViewModel.setSuccess(value = true)
        }
    }

    // Show error if had
    state.errorResult?.ToComposable(
        navigateToLoginPage = navigateToLogin,
        tryRequestAgain = {},
        onDismiss = {
            authenticationViewModel.setError(e = null)
        }
    )

    if (state.isSuccess == true) {
        SuccessDialog(
            message = stringResource(R.string.welcome_word),
            onDismissDialog = {
                authenticationViewModel.setSuccess(value = false)
                onLoginSuccess(state.user ?: UserModel())
            }
        )
    }
    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier) {
                Head(
                    modifier = Modifier.fillMaxWidth(),
                    stringResource(R.string.sing_in_to_your_account), height = 200.dp
                )
                CustomNumberTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 25.dp),
                    label = stringResource(R.string.input_nip),
                    errorMessage = state.nipErrorMessage
                ) {
                    authenticationViewModel.onEvent(event = AuthenticationFormEvent.NipChanged(it))
                }
                CustomTextPasswordField (
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    label = stringResource(R.string.input_password),
                    errorMessage = state.passwordErrorMessage
                ) {
                    authenticationViewModel.onEvent(event = AuthenticationFormEvent.PasswordChanged(it))
                }
                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 20.dp, bottom = 20.dp)
                        .clickable {
                            onNavigateToForgotPasswordScreen()
                        },
                    text = stringResource(R.string.forgot_password) + " ?",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                )
                CustomButtonField(
                    modifier = Modifier.padding(horizontal = 20.dp), buttonName = stringResource(
                        R.string.log_in
                    ),
                    buttonColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        authenticationViewModel.onEvent(event = AuthenticationFormEvent.submitLogin)
                    }
                )
            }
            Text(
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .padding(bottom = 20.dp),
                text = stringResource(R.string.copyright_maspam),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light)
            )

            if (state.isLoading == true) {
                keyboardController?.hide()
                LoadingScreen()
            }
        }
    }

}
