package com.maspam.etrain.training.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maspam.etrain.R

@Composable
fun ConfirmationBottomSheetComponent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.confirmation),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            modifier = Modifier.padding(vertical = 15.dp),
            text = stringResource(R.string.are_you_sure_you_want_to_take_this_post_test),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CustomButtonField(
                modifier = Modifier.weight(0.5f),
                buttonName = stringResource(R.string.cancel),
                buttonColor = MaterialTheme.colorScheme.error,
                onClick = onCancel
            )
            CustomButtonFieldLoad(
                modifier = Modifier.weight(0.5f),
                buttonName = stringResource(R.string.get_started),
                buttonColor = MaterialTheme.colorScheme.primary,
                isLoading = isLoading,
                onClick = onSubmit
            )
        }
    }
}

@Composable
fun ConfirmationBottomSheetFlexComponent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    message: String,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.confirmation),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            modifier = Modifier.padding(vertical = 20.dp),
            text = message,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CustomButtonField(
                modifier = Modifier.weight(0.5f),
                buttonName = stringResource(R.string.cancel),
                buttonColor = MaterialTheme.colorScheme.error,
                onClick = onDismiss
            )
            CustomButtonFieldLoad(
                modifier = Modifier.weight(0.5f),
                buttonName = stringResource(R.string.submit),
                buttonColor = MaterialTheme.colorScheme.primary,
                isLoading = isLoading,
                onClick = onSubmit
            )
        }
    }
}