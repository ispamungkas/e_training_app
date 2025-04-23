package com.maspam.etrain.training.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.maspam.etrain.R

@Composable
fun CustomBottomSheetSectionComponent(
    modifier: Modifier = Modifier,
    message: String,
    isLoading: Boolean,
    label: String,
    onDismiss: () -> Unit,
    onSubmit: (String, Int) -> Unit
) {

    var name by remember { mutableStateOf("") }
    var jp by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis
        )
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth(),
            valueInput = name,
            label = label,
            hint = stringResource(R.string.input_the, label)
        ) {
            name = it
        }
        CustomNumberTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = "JP",
            hint = stringResource(R.string.input_total_jp),
            valueInput = jp.toString()
        ) {
            jp = it.toInt()
        }
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
                isEnable = if (name.isNotBlank() || jp != 0) true else false,
                modifier = Modifier.weight(0.5f),
                buttonName = stringResource(R.string.submit),
                buttonColor = MaterialTheme.colorScheme.primary,
                isLoading = isLoading,
                onClick = {
                    onSubmit(name, jp)
                }
            )
        }
    }

}