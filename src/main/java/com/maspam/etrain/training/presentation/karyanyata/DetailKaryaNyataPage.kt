package com.maspam.etrain.training.presentation.karyanyata

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maspam.etrain.R
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.presentation.component.CustomButtonField
import com.maspam.etrain.training.core.presentation.component.CustomButtonFieldLoad
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.karyanyata.viewmodel.KaryaNyataViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailKaryaNyataPage(
    karyaNyataId: Int,
    karyaNyataViewModel: KaryaNyataViewModel,
    modifier: Modifier = Modifier,
    navigateToLoginPage: () -> Unit,
    onBackPress: () -> Unit
) {

    LaunchedEffect(Unit) {
        karyaNyataViewModel.getKaryaNyataById(karyaNyataId)
    }

    val state by karyaNyataViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    eventListener(
        karyaNyataViewModel.globalEvent
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                karyaNyataViewModel.setError(event.e)
            }
        }
    }

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                karyaNyataViewModel.getKaryaNyataById(karyaNyataId)
            }
        ) {
            karyaNyataViewModel.setError(e = null)
        }
    }

    if (state.isSuccess) {
        SuccessDialog(
            message = "Karya Nyata successfully updated",
        ) {
            onBackPress()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            TopBarWithArrowComponent(
                section = stringResource(R.string.detail_attachment_karya_nyata)
            ) {  }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = false,
            onRefresh = {

            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                Column (
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                ) {
                    Text(text = "Nip:\t: ${state.user?.nip}", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground), modifier = Modifier.padding(vertical = 5.dp))
                    Text(text = "Name\t: ${state.user?.name}", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground), modifier = Modifier.padding(vertical = 5.dp))
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Attachment:\t:", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground), modifier = Modifier.padding(vertical = 5.dp))
                        Spacer(modifier = Modifier.width(50.dp))
                        CustomButtonField(
                            buttonName = stringResource(R.string.show),
                            buttonColor = MaterialTheme.colorScheme.primary
                        ) {
                            println(constructUrl(state.karyaNyata?.att ?: ""))
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(constructUrl(state.karyaNyata?.att ?: "")))
                            context.startActivity(intent, null)
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.BottomCenter)
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    CustomButtonField(
                        modifier = Modifier.weight(0.5f),
                        buttonName = stringResource(R.string.decline),
                        buttonColor = MaterialTheme.colorScheme.error,
                        onClick = {
                            karyaNyataViewModel.updateKaryaNyata("decline")
                        }
                    )
                    CustomButtonFieldLoad(
                        modifier = Modifier.weight(0.5f),
                        buttonName = stringResource(R.string.accept),
                        buttonColor = MaterialTheme.colorScheme.primary,
                        isLoading = false,
                        onClick = {
                            karyaNyataViewModel.updateKaryaNyata("accepted")
                        }
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun Check() {

}