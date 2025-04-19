package com.maspam.etrain.training.presentation.dashboard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maspam.etrain.training.core.presentation.component.ShimmerEffect

@Composable
fun LoadingComponent(
    size: Dp,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShimmerEffect(
            modifier = Modifier.size(size)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp)
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun LoadCheck() {
    LoadingComponent(
        size = 100.dp,
        modifier = Modifier.fillMaxWidth()
    )
}