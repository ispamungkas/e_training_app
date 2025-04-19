package com.maspam.etrain.training.presentation.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScannerButtonComponent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .fillMaxWidth()
                .height(20.dp)
                .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(color = MaterialTheme.colorScheme.onPrimary)
                .shadow(
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    elevation = 2.dp,
                    ambientColor = MaterialTheme.colorScheme.onPrimary,
                )

        )
        Box(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .width((LocalConfiguration.current.screenWidthDp / 2).dp)
                .height(50.dp)
                .clip(shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(color = MaterialTheme.colorScheme.onPrimary)
                .shadow(
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                    elevation = 2.dp,
                    ambientColor = MaterialTheme.colorScheme.onPrimary
                )
        ) {
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .clip(shape = RoundedCornerShape(50.dp))
                    .background(color = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    text = "Scan",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun ScanCheck() {
    ScannerButtonComponent()
}