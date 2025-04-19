package com.maspam.etrain.training.core.presentation.component

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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.maspam.etrain.R
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.presentation.utils.toDateTimeFormatter
import com.maspam.etrain.training.domain.model.EnrollModel

@Composable
fun ListEnrollTrainingComponent(
    data: EnrollModel? = null,
    id: Int? = null,
    imageUri: String? = "",
    typeTraining: String? = "",
    typeTrainingCategory: String? = "",
    due: Long? = null,
    status: String? = "",
    nameTraining: String? = "",
    modifier: Modifier = Modifier,

    onItemClick: (EnrollModel) -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                data?.let {
                    onItemClick(it)
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Image Training
        AsyncImage(
            modifier = Modifier
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                .size(100.dp)
                .clip(shape = RoundedCornerShape(15.dp)),
            model = imageUri?.let {
                constructUrl(it)
            } ?: R.drawable.image_training_sample,
            error = painterResource(R.drawable.image_training_sample),
            contentDescription = "Image Training",
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Training Info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(end = 20.dp, top = 10.dp, bottom = 10.dp),
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
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.primary,
                        painter = painterResource(R.drawable.icon_time),
                        contentDescription = "Icon Category"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = due?.toDateTimeFormatter() ?: stringResource(R.string.due_date),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light)
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
                        tint = MaterialTheme.colorScheme.primary,
                        painter = painterResource(R.drawable.icon_location),
                        contentDescription = "Icon Location"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = typeTrainingCategory ?: stringResource(R.string.implementation),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(color = statusToBackground(status ?: ""))
                ) {
                    Text(
                        text = statusToStringRes(status ?: ""),
                        style = MaterialTheme.typography.labelMedium.copy(
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

@Composable
fun statusToStringRes(status: String): String {
    return when (status) {
        "progress" -> stringResource(R.string.on_process)
        "time out" -> stringResource(R.string.time_out)
        "completed" -> stringResource(R.string.completed)
        else -> stringResource(R.string.need_action)
    }
}

@Composable
fun statusToBackground(status: String): Color {
    return when (status) {
        "progress" -> Color.Blue
        "time out" -> Color.Red
        "completed" -> Color.Green
        else -> Color.Magenta
    }
}
