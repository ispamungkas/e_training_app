package com.maspam.etrain.training.presentation.authentication.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maspam.etrain.R

@Composable
fun Head(modifier: Modifier = Modifier, section: String, height: Dp) {
    Box(modifier = modifier
        .fillMaxWidth()
        .height(height)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.image_head_auth_background),
            contentScale = ContentScale.Crop,
            contentDescription = "Image Head"
        )
        Column(
            modifier = Modifier
                .width(250.dp)
                .padding(horizontal = 20.dp, vertical = 30.dp)
                .align(alignment = Alignment.BottomStart),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = section,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                ),
                overflow = TextOverflow.Clip
            )
        }
    }
}
