package com.maspam.etrain.training.presentation.training

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.domain.model.TrainingModel

@Composable
fun DetailTrainingPage(
    trainingModel: TrainingModel,
    scrollableState: ScrollableState = rememberScrollState(initial = 0),
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopBarWithArrowComponent(
                modifier = Modifier
                    .padding(vertical = 20.dp),
                section = "Detail Training",
                onBackPress = onBackPressed
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

        }
    }
}
