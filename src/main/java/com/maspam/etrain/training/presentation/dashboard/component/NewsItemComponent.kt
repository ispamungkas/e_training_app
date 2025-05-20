package com.maspam.etrain.training.presentation.dashboard.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.utils.toDateTimeVersion2Formatter

@Composable
fun NewsItemComponent(
    image: String? = "",
    newsHeadline: String? = "",
    author: String? = "",
    postDate: Long? = 0L,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = image ?: R.drawable.news_image_sample,
            contentScale = ContentScale.Crop,
            contentDescription = "News Image",
            modifier = Modifier
                .size(110.dp)
                .clip(shape = RoundedCornerShape(15.dp))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.education),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                text = newsHeadline ?: stringResource(R.string.news_headline),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.icon_person_bold),
                    contentDescription = "Icon Person"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${author ?: stringResource(R.string.author)} ~ ${
                        postDate?.toDateTimeVersion2Formatter() ?: stringResource(
                            R.string.xx_january_xxxx
                        )
                    }",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.outline,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun Check() {
    NewsItemComponent()
}