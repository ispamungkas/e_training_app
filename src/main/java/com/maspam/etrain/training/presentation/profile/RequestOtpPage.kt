package com.maspam.etrain.training.presentation.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.CustomButtonFieldLoad
import com.maspam.etrain.training.core.presentation.component.CustomTextField
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.presentation.authentication.viewmodel.ForgotPasswordViewModel

@Composable
fun RequestOtpPage(
    nip: String,
    forgotPasswordViewModel: ForgotPasswordViewModel,
    onNextPage: () -> Unit,
    navigateToLoginPage: () -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {

    val state by forgotPasswordViewModel.state.collectAsStateWithLifecycle()

    if (state.isSuccess) {
        onNextPage()
        forgotPasswordViewModel.setSuccess(false)
    }

    if (state.isError) {
        state.error?.ToComposable(
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                forgotPasswordViewModel.onDismissError()
                forgotPasswordViewModel.requestOtp(nip = nip)
            },
            onDismiss = { forgotPasswordViewModel.onDismissError() }
        )
    }

    Scaffold(
        modifier = modifier
            .systemBarsPadding(),
        topBar = {
            TopBarWithArrowComponent(section = "Change Password", onBackPress = onBackPressed)
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp, horizontal = 20.dp),
                    text = "To change the password, a code is required to verify the user. The code will be sent via user email!",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal)
                )
                CustomTextField(
                    valueInput = state.user?.email ?: "Email",
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    label = "Email"
                ) {}
                CustomButtonFieldLoad(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    buttonName = stringResource(R.string.submit),
                    isLoading = false,
                    isEnable = true,
                    onClick = {
                        forgotPasswordViewModel.requestOtp(nip = nip)
                    }
                )
            }
        }
    }
}