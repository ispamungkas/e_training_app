package com.maspam.etrain.training.core.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maspam.etrain.R

@Composable
fun CustomButtonFieldLoad(isEnable: Boolean = true, modifier: Modifier = Modifier, buttonName: String, buttonColor: Color = MaterialTheme.colorScheme.primary,  isLoading: Boolean = false, onClick: () -> Unit) {
    FilledIconButton(
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = buttonColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .bounceClick(),
        enabled = isEnable,
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
    ) {
        Text(
            text = if (isLoading) stringResource(R.string.loading) else buttonName ,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            ),
        )
    }
}

@Composable
fun CustomButtonFieldLoad2(isEnable: Boolean = true, modifier: Modifier = Modifier, buttonName: String, isLoading: Boolean = false, onClick: () -> Unit) {
    FilledIconButton(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .bounceClick(),
        enabled = isEnable,
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
    ) {
        Text(
            text = if (isLoading) stringResource(R.string.loading) else buttonName ,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            ),
        )
    }
}

@Composable
fun CustomButtonField(modifier: Modifier = Modifier, buttonName: String, buttonColor: Color, onClick: () -> Unit) {
    FilledIconButton(
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = buttonColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .bounceClick(),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
    ) {
        Text(
            text = buttonName ,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            ),
        )
    }
}

@Preview
@Composable
private fun Check() {
    CustomButtonField(
        buttonColor = Color.Black,
        buttonName = "sef"
    ) { }
}