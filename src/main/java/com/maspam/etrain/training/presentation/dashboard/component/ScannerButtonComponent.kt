package com.maspam.etrain.training.presentation.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maspam.etrain.training.core.presentation.component.CustomRunningTextButton

@Composable
fun ScannerButtonComponent(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(topStart = 100.dp, topEnd = 100.dp)
                )
                .align(alignment = Alignment.BottomCenter)
                .shadow(elevation = 10.dp, ambientColor = Color.Black, shape = RoundedCornerShape(topStart = 70.dp, topEnd = 70.dp))
        )
        Box(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .height(70.dp)
                .width(250.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(topStart = 70.dp, topEnd = 70.dp)
                )
        ) {
            CustomRunningTextButton(
                modifier = Modifier
                    .width(100.dp)
                    .align(alignment = Alignment.Center)
                    .shadow(elevation = 5.dp, ambientColor = Color.Black, shape = RoundedCornerShape(100)),
                buttonName = "Scan Certificate",
                buttonColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                onClick()
            }
        }
    }
}