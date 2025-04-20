package com.maspam.etrain.training.presentation.taketraining.contentitem

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maspam.etrain.training.core.presentation.component.TopBarWithStartImage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun KaryaNyataContentPage(modifier: Modifier = Modifier, onMenuClicked: () -> Unit) {
    Scaffold(
        modifier = modifier
            .systemBarsPadding(),
        topBar = {
            TopBarWithStartImage(
                name = "Karya Nyata"
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
                    .padding(20.dp)
            ) {
                Text(
                    text = "Please input your assignment for “Karya Nyata” that contain on this training material !!!",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                        .height(100.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(15.dp)
                        )
                ) {
                    Text(
                        text = "Choose your file",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onPrimary),
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                    )
                }
                Text(
                    text = "* The file must be .pdf format",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.error)
                )
                FilledIconButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 20.dp),
                    onClick = {

                    }
                ) {
                    Text(
                        text = "Submit",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onPrimary)
                    )
                }
            }

        }
    }
}