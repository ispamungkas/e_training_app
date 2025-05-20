package com.maspam.etrain.training.presentation.dashboard.extended

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.ConfirmationBottomSheetFlexComponent
import com.maspam.etrain.training.core.presentation.component.CustomTextField
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.bounceClick
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.presentation.dashboard.component.UserAccountComponent
import com.maspam.etrain.training.presentation.dashboard.viewmodel.UserAccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListUserAccountPage(
    sheetState: SheetState = rememberModalBottomSheetState(),
    userAccountViewModel: UserAccountViewModel,
    modifier: Modifier = Modifier,
    navigateToLoginPage: () -> Unit,
    navigateToRegisterPage: () -> Unit
) {

    val state by userAccountViewModel.state.collectAsStateWithLifecycle()

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                userAccountViewModel.apply {
                    setError(e = null)
                    getListUser()
                }
            },
            onDismiss = { userAccountViewModel.setError(e = null) },
        )
    }

    if (state.isSuccess) {
        SuccessDialog(
            message = stringResource(R.string.account_successfully_updated)
        ) {
            userAccountViewModel.run {
                dismissAlert()
                setShowModal(false)
            }
        }
    }

    Scaffold (
        modifier = modifier
            .systemBarsPadding(),
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .bounceClick()
                    .clickable {
                        navigateToRegisterPage()
                    }
            ) {
                Icon(
                    modifier = Modifier
                        .size(25.dp)
                        .align(alignment = Alignment.Center),
                    imageVector = Lucide.Plus,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Icon Add"
                )
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefresh,
            onRefresh =  {
                userAccountViewModel.refresh()
            }
        ) {
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                item {
                    CustomTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                        label = stringResource(R.string.search),
                        singleLine = true,
                        hint = stringResource(R.string.search)
                    ) { value ->
                        userAccountViewModel.onSearch(value)
                    }
                }

                items(items = state.filteredData ?: emptyList(), key = { it.id ?: 0 }) { data ->
                    UserAccountComponent(
                        userModel = data,
                        onReset = {
                            userAccountViewModel.apply {
                                setShowModal(true)
                                setSelectedNip(data.nip ?: "")
                            }
                        }
                    )
                }
            }
        }

        if (state.isBottomSheetShow) {
            ModalBottomSheet(
                onDismissRequest = {
                    userAccountViewModel.setShowModal(false)
                },
                sheetState = sheetState,
            ) {
               ConfirmationBottomSheetFlexComponent(
                   message = stringResource(R.string.are_you_sure_to_reset_the_password),
                   isLoading = state.isLoading,
                   onDismiss = {
                       userAccountViewModel.setShowModal(false)
                   }
               ) {
                   userAccountViewModel.resetPassword(state.selectedNip ?: "")
               }

            }
        }
    }
}