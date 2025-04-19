package com.maspam.etrain.training.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.maspam.etrain.training.core.presentation.component.CustomButtonField
import com.maspam.etrain.training.core.presentation.component.CustomTextPasswordField
import com.maspam.etrain.training.core.presentation.component.LoadingScreen
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.presentation.profile.event.ChangePasswordProfileEvent
import com.maspam.etrain.training.presentation.profile.viewmodel.UpdatePasswordProfileViewModel

@Composable
fun ChangePasswordProfilePage(
    updatePasswordProfileViewModel: UpdatePasswordProfileViewModel,
    onSuccessUpdate: () -> Unit,
    navigateToLoginPage: () -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .systemBarsPadding(),
        topBar = {
            TopBarWithArrowComponent(
                section = "Change Password",
                onBackPress = onBackPressed
            )
        }
    ) { innerPadding ->
        val state by updatePasswordProfileViewModel.state.collectAsStateWithLifecycle()
        
        eventListener(
            event = updatePasswordProfileViewModel.event
        ) { event ->
            when (event) {
                is UpdatePasswordProfileViewModel.OnChangePasswordProfileEvent.Failure -> {
                    updatePasswordProfileViewModel.setError(e = event.e)
                }
                UpdatePasswordProfileViewModel.OnChangePasswordProfileEvent.Success -> onSuccessUpdate()
            }
        }
        
        if (state.error != null) {
            state.error?.ToComposable(
                isLoading = state.isLoading,
                navigateToLoginPage = navigateToLoginPage,
                tryRequestAgain = { updatePasswordProfileViewModel.changePassword() },
                onDismiss = { updatePasswordProfileViewModel.setError(null) }
            )
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Lucide.Info, contentDescription = "Information")
                    Text(
                        text = stringResource(R.string.confirm_new_password_information),
                        overflow = TextOverflow.Clip,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light),
                    )
                }
                Divider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 1.dp)
                Spacer(modifier = Modifier.height(20.dp))
                CustomTextPasswordField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    errorMessage = state.passwordError,
                    label = stringResource(R.string.new_passsword)
                ) {
                    updatePasswordProfileViewModel.onAction(action = ChangePasswordProfileEvent.PasswordForm(it))
                }
                CustomTextPasswordField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    errorMessage = state.confirmPasswordError,
                    label = stringResource(R.string.confirm_new_password)
                ) {
                    updatePasswordProfileViewModel.onAction(action = ChangePasswordProfileEvent.ConfirmPasswordForm(it))
                }
                CustomButtonField(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                    buttonName = stringResource(R.string.submit),
                    buttonColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        updatePasswordProfileViewModel.onAction(action = ChangePasswordProfileEvent.submit)
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
            if (state.isLoading) {
                LoadingScreen()
            }
        }
    }
}
