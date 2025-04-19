package com.maspam.etrain.training.presentation.dashboard.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.maspam.etrain.BuildConfig
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.CustomButtonField
import com.maspam.etrain.training.core.presentation.component.CustomButtonFieldLoad
import com.maspam.etrain.training.core.presentation.utils.ExpandableText
import com.maspam.etrain.training.core.presentation.utils.toDateTimeFormatter
import com.maspam.etrain.ui.theme.poppinsFontFamily

@Composable
fun ModalTrainingDetailComponent(
    image: String? = "",
    typeTraining: String? = "",
    due: Long? = 0L,
    isOpen: Boolean? = true,
    trainingName: String? = "",
    typeTrainingAtt: String? = "",
    totalTaken: Int? = 0,
    totalJP: Int? = 0,
    description: String? = "",
    isLoading: Boolean = false,
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .scrollable(state = state, orientation = Orientation.Vertical)
    ) {
        image?.let {
            AsyncImage(
                model = BuildConfig.BASE_URL + image,
                contentDescription = "Training Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 10.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
            )
        } ?: Image(
            painter = painterResource(R.drawable.image_training_sample),
            contentScale = ContentScale.Crop,
            contentDescription = "Training Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(top = 10.dp)
                .clip(shape = RoundedCornerShape(15.dp))
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(R.drawable.icon_category),
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
                    painter = painterResource(R.drawable.icon_time),
                    contentDescription = "Icon Time"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = due?.toDateTimeFormatter() ?: stringResource(R.string.due_date),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light)
                )
            }
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(color = if (isOpen == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 35.dp, vertical = 3.dp),
                    text = if (isOpen == true) stringResource(R.string.open) else stringResource(R.string.close),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }


        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            text = trainingName ?: stringResource(R.string.name_training),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(R.drawable.icon_location),
                    contentDescription = "Icon Location"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = typeTrainingAtt ?: stringResource(R.string.implementation),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(R.drawable.icon_person),
                    contentDescription = "Icon Person"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = totalTaken.toString(),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(R.drawable.icon_time_reverse),
                    contentDescription = "Icon JP"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "${totalJP} JP",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )

        Spacer(modifier = Modifier.height(10.dp))

        ExpandableText(
            text = description ?: "${stringResource(R.string.description)} Training",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
            collapsedMaxLine = 6,
            showMoreText = "... ${stringResource(R.string.see_more)}",
            showMoreStyle = SpanStyle(
                color = MaterialTheme.colorScheme.outline,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                fontFamily = poppinsFontFamily
            ),
            showLessText = stringResource(R.string.see_less),
            showLessStyle = SpanStyle(
                color = MaterialTheme.colorScheme.outline,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                fontFamily = poppinsFontFamily
            ),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.are_you_sure_want_to_take_this_training),
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

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

        Spacer(modifier = Modifier.height(15.dp))
    }
}