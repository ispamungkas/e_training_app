package com.maspam.etrain.training.presentation.taketraining.contentitem

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.presentation.component.TopBarWithStartImage

@Composable
fun ContentCertificatePage(
    modifier: Modifier = Modifier,
    linkCertificate: String?,
    onButtonDownloadClicked: () -> Unit,
    onMenuClicked: () -> Unit
) {

    linkCertificate?.let {
        val url = it.removeRange(0..5)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(constructUrl(url)))
        val context = LocalContext.current
        context.startActivity(intent, null)
    }

    Scaffold(
        modifier = modifier
            .systemBarsPadding(),
        topBar = {
            TopBarWithStartImage(
                name = "Certificate"
            ) {
                onMenuClicked()
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Certificate Completion",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Divider(
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp)
                )
                FilledIconButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        onButtonDownloadClicked()
                    }
                ) {
                    Text(
                        text = "Download",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }

            }
        }
    }
}