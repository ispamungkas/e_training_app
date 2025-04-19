package com.maspam.etrain.training.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent

@Composable
fun InformationPage(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .systemBarsPadding(),
        topBar = {
            TopBarWithArrowComponent(
                section = stringResource(R.string.app_information),
                onBackPress = onBackPressed
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(alignment = Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.app_info_image),
                    contentDescription = "App Info",
                    modifier = Modifier
                        .height(228.dp)
                        .width(200.dp)
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.app_info_description),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal)
                )
            }

            Text(
                modifier = Modifier.align(alignment = Alignment.BottomCenter).padding(bottom = 30.dp),
                text = "Version 0.0.1",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal)
            )
        }
    }
}
