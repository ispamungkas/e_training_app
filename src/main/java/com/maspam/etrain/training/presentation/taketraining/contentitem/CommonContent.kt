package com.maspam.etrain.training.presentation.taketraining.contentitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.composables.icons.lucide.CheckCheck
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Menu
import com.composables.icons.lucide.Timer
import com.maspam.etrain.R
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.presentation.component.HtmlText
import com.maspam.etrain.training.core.presentation.component.sanitizeHtml
import com.maspam.etrain.training.domain.model.TopicModel

@Composable
fun CommonContentPage(
    modifier: Modifier = Modifier,
    topicModel: TopicModel,
    isCheck: Boolean,
    isLoading: Boolean,
    markAsCompletedPressed: () -> Unit,
    onMenuClicked: () -> Unit
) {

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding)
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        alignment = Alignment.TopStart,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        model = constructUrl(topicModel.topicImage ?: "") ,
                        error = painterResource(R.drawable.placeholder_image_sample),
                        placeholder = painterResource(R.drawable.placeholder_image_sample),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Image Content",
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = 25.dp, start = 20.dp)
                            .size(50.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(100)
                            )
                            .align(alignment = Alignment.TopStart)
                            .clickable {
                                onMenuClicked()
                            }
                    ) {
                        Icon(
                            modifier = Modifier
                                .align(alignment = Alignment.Center),
                            imageVector = Lucide.Menu,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Icon menu"
                        )
                    }
                    Card(
                        colors = CardDefaults.cardColors()
                            .copy(containerColor = MaterialTheme.colorScheme.background),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .align(Alignment.TopCenter)
                            .padding(top = 210.dp),
                        shape = RoundedCornerShape(
                            topStart = 15.dp,
                            topEnd = 15.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(
                                    horizontal = 20.dp,
                                    vertical = 25.dp
                                )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = topicModel.name ?: "Topic Name",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Lucide.Timer,
                                        contentDescription = "Icon Timer",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = stringResource(R.string._45_minutes),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium)
                                    )
                                }
                            }
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 15.dp)
                            )
                            key(topicModel.id) {
                                HtmlText(
                                    modifier = Modifier.fillMaxWidth(),
                                    html = sanitizeHtml(topicModel.content ?: "Topic Content")
                                )
                            }

                            if (!isCheck) {
                                Box(
                                    modifier = Modifier

                                        .background(
                                            Color.Green.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .align(alignment = Alignment.End)
                                        .clip(shape = RoundedCornerShape(15.dp))
                                        .clickable {
                                            markAsCompletedPressed()
                                        }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(horizontal = 20.dp, vertical = 10.dp)
                                    ) {
                                        Icon(
                                            imageVector = Lucide.CheckCheck,
                                            tint = Color.Green,
                                            contentDescription = "Icon Check"
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "Mark as completed",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = Color.Green,
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (isLoading) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}
