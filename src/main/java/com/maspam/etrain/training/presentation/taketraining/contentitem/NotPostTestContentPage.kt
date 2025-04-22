package com.maspam.etrain.training.presentation.taketraining.contentitem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maspam.etrain.training.core.presentation.component.ConfirmationBottomSheetComponent
import com.maspam.etrain.training.core.presentation.component.TopBarWithHumbergerIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotPostTestContentPage(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onSubmit: () -> Unit,
    onMenuClicked: () -> Unit
) {
    var bottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier
            .systemBarsPadding(),
        topBar = {
            TopBarWithHumbergerIcon (
                name = "Post Test"
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
                    text = "Post Test",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                )
                Divider(
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp)
                )
                Text(
                    text = "This post test refers to the learning results from several sections that have been given previously.\n" +
                            "\n" +
                            "You can take this post test only one change, if you’re sure please click “Continue” to take the post test !",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
                )
                FilledIconButton(
                    modifier = Modifier
                        .width(100.dp)
                        .height(60.dp)
                        .align(alignment = Alignment.End)
                        .padding(top = 20.dp),
                    onClick = {
                        bottomSheet = true
                    }
                ) {
                    Text(
                        text = "Continue",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onPrimary)
                    )
                }
            }
        }

        if (bottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    bottomSheet = false
                },
                sheetState = rememberModalBottomSheetState()
            ) {
                ConfirmationBottomSheetComponent(
                    onCancel = {
                        bottomSheet = false
                    },
                    isLoading = isLoading,
                    onSubmit = onSubmit
                )
            }
        }
    }
}