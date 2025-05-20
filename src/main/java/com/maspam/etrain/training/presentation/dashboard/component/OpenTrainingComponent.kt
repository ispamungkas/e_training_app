package com.maspam.etrain.training.presentation.dashboard.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Globe
import com.composables.icons.lucide.Lucide
import com.maspam.etrain.R
import com.maspam.etrain.training.domain.model.TrainingModel

@Composable
fun OpenTrainingComponent(
    currentTraining: List<Int> = emptyList(),
    dataTraining: List<TrainingModel> = emptyList(),
    isLoading: Boolean = false,
    onClickItem: (Int) -> Unit,
    navigateToListOpenTraining: () -> Unit,
    modifier: Modifier = Modifier
) {

    val filtered = dataTraining.filter { !currentTraining.contains(it.id) }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors()
            .copy(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Lucide.Globe,
                    contentDescription = "Icon Training",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.training),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(15.dp))
                    .clickable {
                        navigateToListOpenTraining()
                    }
            ) {
                Text(
                    text = stringResource(R.string.see_more),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Light
                    )
                )
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    painter = painterResource(R.drawable.icon_next),
                    contentDescription = "Icon Next",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Divider(
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(15.dp))

        if (isLoading) {
            LoadingComponent(size = 100.dp, modifier.fillMaxWidth())
        } else {
            if (filtered.isNotEmpty()) {
                filtered.forEach { data ->
                    OpenTrainingItemComponent(
                        modifier = Modifier.padding(bottom = 15.dp, end = 20.dp, start = 20.dp),
                        imageUri = data.image,
                        nameTraining = data.name,
                        typeTraining = data.typeTraining,
                        typeTrainingCategory = data.typeTrainingCategory,
                        isOpen = data.isOpen,
                        id = data.id,
                        totalPerson = data.totalTaken,
                        due = data.due,
                        onItemClick = onClickItem
                    )
                }
            } else {
                Text(
                    stringResource(R.string.no_training_has_open),
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(13.dp))
    }
}
