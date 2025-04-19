package com.maspam.etrain.training.presentation.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.LoadingScreen
import com.maspam.etrain.training.core.presentation.component.OtpInputField
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.presentation.authentication.component.Head
import com.maspam.etrain.training.presentation.authentication.event.OTPEvent
import com.maspam.etrain.training.presentation.authentication.state.OTPState
import com.maspam.etrain.training.presentation.authentication.viewmodel.OTPViewModel

sealed interface OTPAction {
    data class OnEnterNumber(val number: Int?, val index: Int) : OTPAction
    data class OnChangeFieldFocused(val index: Int) : OTPAction
    data object OnKeyboardBack : OTPAction
}

@Composable
fun OTPPage(
    nip: String? = "",
    state: OTPState,
    focusRequesters: List<FocusRequester>,
    otpViewModel: OTPViewModel,
    onNextPage : (String) -> Unit,
    navigateToLoginPage : () -> Unit,
    onAction: (OTPAction) -> Unit,
    modifier: Modifier = Modifier
) {

    eventListener(
        otpViewModel.eventOtp
    ) { res ->
        when (res) {
            is OTPEvent.Error -> {
                otpViewModel.setError(e = res.e)
            }
            is OTPEvent.Success -> {
                nip?.let{
                    onNextPage(it)
                }
            }
        }
    }

    // Catching Error
    state.error?.ToComposable(
        navigateToLoginPage = navigateToLoginPage,
        tryRequestAgain =  {},
        onDismiss = {
            otpViewModel.setError(e = null)
        }
    )

    Box(modifier = modifier) {
        Column(
            modifier = Modifier,
        ) {
            Head(section = stringResource(R.string.forgot_password), height = 150.dp)
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Lucide.Info, contentDescription = "Information")
                Text(
                    text = stringResource(R.string.otp_information),
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light),
                )
            }
            Divider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 1.dp)
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                state.code.forEachIndexed { index, number ->
                    OtpInputField(
                        number = number,
                        focusRequester = focusRequesters[index],
                        onFocusChanged = { isFocus ->
                            if (isFocus) {
                                onAction(OTPAction.OnChangeFieldFocused(index))
                            }
                        },
                        onNumberChanged = { newNumber ->
                            onAction(OTPAction.OnEnterNumber(newNumber, index))
                        },
                        onBackPress = {
                            onAction(OTPAction.OnKeyboardBack)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    )
                }
            }

        }
        if (state.isLoading == true) {
            LoadingScreen()
        }
    }
}

