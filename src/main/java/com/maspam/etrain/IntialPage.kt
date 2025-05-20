package com.maspam.etrain

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.maspam.etrain.training.presentation.authentication.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InitialPage(
    authenticationViewModel: AuthenticationViewModel,
    hasUser: (String) -> Unit,
    login: () -> Unit,
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        authenticationViewModel.getUser()
    }

    val state by authenticationViewModel.initialState.collectAsState()

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->

        LaunchedEffect(true) {
            scope.launch {
                delay(1000)
                state.userSession?.token?.let {
                    hasUser(state.userSession?.role ?: "")
                } ?: login()
            }
        }

        Box(modifier = Modifier.padding(innerPadding))
    }
}