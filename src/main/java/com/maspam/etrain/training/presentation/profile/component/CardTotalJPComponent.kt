package com.maspam.etrain.training.presentation.profile.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.bounceClick

@Composable
fun CardTotalJPComponent(
    totalJp: Int? = 0,
    trainJp: Int? = 0,
    workshopJp: Int? = 0,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card (
        colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
        border = BorderStroke(1.dp, color =MaterialTheme.colorScheme.outline),
        modifier = modifier
            .fillMaxWidth()
            .bounceClick()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(stringResource(R.string.total_training_hours), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal))
            Text("$totalJp JP", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp))
            Spacer(modifier = Modifier.height(5.dp))
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(modifier = Modifier
                    .size(10.dp)
                    .clip(shape = RoundedCornerShape(100))
                    .background(color = MaterialTheme.colorScheme.primary))
                Spacer(modifier = Modifier.width(13.dp))
                Text("$trainJp Jp ~ ${stringResource(R.string.training)}", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(modifier = Modifier
                    .size(10.dp)
                    .clip(shape = RoundedCornerShape(100))
                    .background(color = MaterialTheme.colorScheme.inversePrimary))
                Spacer(modifier = Modifier.width(13.dp))
                Text("$workshopJp Jp ~ ${stringResource(R.string.workshop)}", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal))
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun CardTotalJPComponentPrev() {
    CardTotalJPComponent() {}
}