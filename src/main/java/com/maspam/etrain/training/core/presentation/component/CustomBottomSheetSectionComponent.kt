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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.maspam.etrain.R

@Composable
fun CustomBottomSheetSectionComponent(
    modifier: Modifier = Modifier,
    message: String,
    isLoading: Boolean,
    initialName: String? = "",
    initialJp: Int? = 0,
    label: String,
    onDismiss: () -> Unit,
    onSubmit: (String, Int) -> Unit
) {

    var name by remember { mutableStateOf(initialName) }
    var jp by remember { mutableStateOf(initialJp) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            text = message,
            textAlign = TextAlign.Center,
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
            if (it.isNotBlank()) {
                jp = it.toInt()
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CustomButtonField(
                modifier = Modifier.weight(0.5f),
                buttonName = stringResource(R.string.cancel),
                buttonColor = MaterialTheme.colorScheme.error,
                onClick = onDismiss
            )
            CustomButtonFieldLoad(
                isEnable = if (name?.isNotBlank() == true || jp != 0) true else false,
                modifier = Modifier.weight(0.5f),
                buttonName = stringResource(R.string.submit),
                buttonColor = MaterialTheme.colorScheme.primary,
                isLoading = isLoading,
                onClick = {
                    onSubmit(name ?: "", jp ?: 0)
                }
            )
        }
    }

}

@Composable
fun CustomBottomSheetSectionComponent2(
    modifier: Modifier = Modifier,
    message: String,
    isLoading: Boolean,
    initialName: String? = "",
    label: String,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {

    var name by remember { mutableStateOf(initialName) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            text = message,
            textAlign = TextAlign.Center,
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
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CustomButtonField(
                modifier = Modifier.weight(0.5f),
                buttonName = stringResource(R.string.cancel),
                buttonColor = MaterialTheme.colorScheme.error,
                onClick = onDismiss
            )
            CustomButtonFieldLoad(
                isEnable = if (name?.isNotBlank() == true) true else false,
                modifier = Modifier.weight(0.5f),
                buttonName = stringResource(R.string.submit),
                buttonColor = MaterialTheme.colorScheme.primary,
                isLoading = isLoading,
                onClick = {
                    onSubmit(name ?: "")
                }
            )
        }
    }

}