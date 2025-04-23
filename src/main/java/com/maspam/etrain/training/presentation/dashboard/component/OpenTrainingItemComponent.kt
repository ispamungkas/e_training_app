package com.maspam.etrain.training.presentation.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.composables.icons.lucide.LocateFixed
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.TimerReset
import com.composables.icons.lucide.User
import com.maspam.etrain.R
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.presentation.utils.toDateTimeFormatter

@Composable
fun OpenTrainingItemComponent(
    id: Int? = null,
    imageUri: String? = "",
    typeTraining: String? = "",
    typeTrainingCategory: String? = "",
    totalPerson: Int? = 0,
    due: Long? = null,
    isOpen: Boolean? = false,
    nameTraining: String? = "",
    modifier: Modifier = Modifier,

    onItemClick: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .clickable {
                id?.let {
                    onItemClick(it)
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Image Training
        AsyncImage(
            modifier = Modifier
                .size(100.dp)
                .clip(shape = RoundedCornerShape(15.dp)),
            model = imageUri?.let {
                constructUrl(it)
            } ?: R.drawable.image_training_sample,
            contentDescription = "Image Training",
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Training Info
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        painter = painterResource(R.drawable.icon_category),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Icon Category"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = typeTraining ?: stringResource(R.string.type_training),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        imageVector = Lucide.TimerReset,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Icon Category"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = due?.toDateTimeFormatter() ?: stringResource(R.string.due_date),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light)
                    )
                }
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f),
                text = nameTraining ?: stringResource(R.string.name_training),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        imageVector = Lucide.LocateFixed,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Icon Location"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = typeTrainingCategory ?: stringResource(R.string.implementation),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        imageVector = Lucide.User,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Icon Person"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = totalPerson.toString(),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(color = if (isOpen == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                ) {
                    Text(
                        text = if (isOpen == true) stringResource(R.string.open) else stringResource(
                            R.string.close
                        ),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                        ),
                        modifier = Modifier.padding(horizontal = 13.dp, vertical = 3.dp),
                    )
                }
            }
        }

    }
}


@Preview(
    showBackground = true
)
@Composable
private fun Check() {
    OpenTrainingItemComponent {}
}