package com.maspam.etrain.training.presentation.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.CustomButtonFieldLoad
import com.maspam.etrain.training.core.presentation.component.CustomNumberTextField
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.presentation.authentication.component.Head
import com.maspam.etrain.training.presentation.authentication.viewmodel.ForgotPasswordViewModel

@Composable
fun ForgotPasswordPage(
    forgotPasswordViewModel: ForgotPasswordViewModel,
    modifier: Modifier = Modifier,
    onNextPage: (String) -> Unit,
    navigateToLoginPage: () -> Unit
) {
    val state by forgotPasswordViewModel.state.collectAsStateWithLifecycle()
    var value by remember { mutableStateOf("") }

    if (state.isSuccess) {
        onNextPage(value)
        forgotPasswordViewModel.setSuccess(false)
    }

    if (state.isError) {
        state.error?.ToComposable(
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                forgotPasswordViewModel.onDismissError()
                forgotPasswordViewModel.requestOtp(nip = value)
            },
            onDismiss = { forgotPasswordViewModel.onDismissError() }
        )
    }

    Scaffold(modifier = modifier) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column {
                Head(section = stringResource(R.string.forgot_password), height = 150.dp)
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Lucide.Info, contentDescription = "Information")
                    Text(
                        text = stringResource(R.string.please_input_your_correct_nip_that_was_generated),
                        overflow = TextOverflow.Clip,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light),
                    )
                }
                Divider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 1.dp)
                CustomNumberTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    label = stringResource(R.string.input_nip)
                ) {
                    value = it
                }
                CustomButtonFieldLoad(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    buttonName = stringResource(R.string.submit),
                    isLoading = state.isLoading,
                    isEnable = value.isNotEmpty(),
                    onClick = {
                        forgotPasswordViewModel.requestOtp(nip = value)
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
        }
    }
}