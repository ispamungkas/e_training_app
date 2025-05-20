package com.maspam.etrain.training.presentation.karyanyata.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.setColorBackground
import com.maspam.etrain.training.core.presentation.component.setColorItem

@Composable
fun KaryaNyataItemComponent(
    name: String? = "",
    status: String? = "",
    navigateToDetailKaryaNyata: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name ?: stringResource(R.string.username),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .background(
                            color = setColorBackground(status ?: ""),
                            shape = RoundedCornerShape(15.dp)
                        )
                ) {
                    Text(
                        text = status ?: "Not yet",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = setColorItem(status ?: "")
                        ),
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .clickable {
                            navigateToDetailKaryaNyata()
                        }
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(15.dp)
                        )
                ) {
                    Text(
                        text = "Check",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
                    )
                }
            }
        }
    }
}

