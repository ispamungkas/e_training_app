package com.maspam.etrain.training.presentation.training

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.composables.icons.lucide.LocateFixed
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.TimerReset
import com.maspam.etrain.R
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.presentation.component.CustomButtonFieldLoad
import com.maspam.etrain.training.core.presentation.utils.ExpandableText
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.core.presentation.utils.toDateTimeVersion2Formatter
import com.maspam.etrain.training.domain.model.SectionModel
import com.maspam.etrain.training.domain.model.TrainingModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.viewmodel.DetailTrainingViewModel
import com.maspam.etrain.ui.theme.poppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTrainingPage(
    training: TrainingModel,
    detailTrainingViewModel: DetailTrainingViewModel,
    navigateToLoginPage: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToListSection: (List<SectionModel>) -> Unit,
) {

    LaunchedEffect(true) {
        detailTrainingViewModel.setInitialValue(training)
    }

    val state by detailTrainingViewModel.state.collectAsStateWithLifecycle()
    val detailTraining = state.selected

    eventListener(
        detailTrainingViewModel.globalEvent
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                detailTrainingViewModel.setError(event.e)
            }
        }
    }

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                detailTrainingViewModel.apply {
                    setError(e = null)
                    getTrainingById()
                }
            }
        ) {
            detailTrainingViewModel.setError(e = null)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {

        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefresh,
            onRefresh = {
                detailTrainingViewModel.run {
                    refresh()
                    getTrainingById()
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentScale = ContentScale.Crop,
                                error = painterResource(R.drawable.placeholder_image_sample),
                                placeholder = painterResource(R.drawable.placeholder_image_sample),
                                model = constructUrl(detailTraining?.image ?: ""),
                                contentDescription = "Training Image"
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(top = 15.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        tint = MaterialTheme.colorScheme.primary,
                                        imageVector = Lucide.LocateFixed,
                                        contentDescription = "Icon Location"
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = detailTraining?.typeTrainingCategory
                                            ?: stringResource(R.string.implementation),
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal)
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        tint = MaterialTheme.colorScheme.primary,
                                        painter = painterResource(R.drawable.icon_category),
                                        contentDescription = "Icon Category"
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = detailTraining?.typeTraining
                                            ?: stringResource(R.string.type_training),
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal)
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        tint = MaterialTheme.colorScheme.primary,
                                        imageVector = Lucide.TimerReset,
                                        contentDescription = "Icon Category"
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = detailTraining?.due?.toDateTimeVersion2Formatter()
                                            ?: stringResource(R.string.type_training),
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal)
                                    )
                                }
                            }

                            Text(
                                modifier = Modifier.padding(vertical = 15.dp, horizontal = 20.dp),
                                text = detailTraining?.name ?: stringResource(R.string.training),
                                overflow = TextOverflow.Clip,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                            )

                            Text(
                                modifier = Modifier.padding(bottom = 15.dp, start = 20.dp, end = 20.dp),
                                text = stringResource(R.string.narasumber),
                                overflow = TextOverflow.Clip,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                            )

                            Text(
                                modifier = Modifier.padding(top = 5.dp, start = 20.dp, end = 20.dp),
                                text = stringResource(R.string.description),
                                overflow = TextOverflow.Clip,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )

                            ExpandableText(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 15.dp, horizontal = 20.dp),
                                text = detailTraining?.desc
                                    ?: "${stringResource(R.string.description)} Training",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onBackground
                                ),
                                collapsedMaxLine = 6,
                                showMoreText = "... ${stringResource(R.string.see_more)}",
                                showMoreStyle = SpanStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    fontFamily = poppinsFontFamily
                                ),
                                showLessText = stringResource(R.string.see_less),
                                showLessStyle = SpanStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    fontFamily = poppinsFontFamily
                                ),
                                fontSize = 12.sp
                            )

                            Text(
                                modifier = Modifier.padding(vertical = 10.dp),
                                text = stringResource(R.string.materi),
                                overflow = TextOverflow.Clip,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )

                            Row(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = if ((detailTraining?.sections?.size
                                                    ?: 0) >= 1
                                            ) {
                                                Color.Green.copy(alpha = 0.1f)
                                            } else {
                                                MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                                            },
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                ) {
                                    Text(
                                        modifier = Modifier.padding(
                                            horizontal = 34.dp,
                                            vertical = 5.dp
                                        ),
                                        text = if ((detailTraining?.sections?.size ?: 0) >= 1) {
                                            stringResource(R.string.completed)
                                        } else {
                                            stringResource(R.string.empty)
                                        },
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Normal,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                        .clip(shape = RoundedCornerShape(10.dp))
                                ) {
                                    Text(
                                        modifier = Modifier.padding(
                                            horizontal = 34.dp,
                                            vertical = 5.dp
                                        ),
                                        text = stringResource(R.string.add),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Normal,
                                        )
                                    )
                                }

                            }

                            Text(
                                modifier = Modifier.padding(top = 30.dp, start = 20.dp, end = 20.dp),
                                text = stringResource(R.string.materi),
                                overflow = TextOverflow.Clip,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )

                            Row(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = if ((detailTraining?.sections?.size
                                                    ?: 0) >= 1
                                            ) {
                                                Color.Green.copy(alpha = 0.1f)
                                            } else {
                                                MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                                            },
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                ) {
                                    Text(
                                        modifier = Modifier.padding(
                                            horizontal = 34.dp,
                                            vertical = 5.dp
                                        ),
                                        text = if ((detailTraining?.sections?.size ?: 0) >= 1) {
                                            stringResource(R.string.completed)
                                        } else {
                                            stringResource(R.string.empty)
                                        },
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Normal,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                        .clip(shape = RoundedCornerShape(10.dp))
                                        .clickable {
                                            navigateToListSection(
                                                detailTraining?.sections ?: emptyList()
                                            )
                                        }
                                ) {
                                    Text(
                                        modifier = Modifier.padding(
                                            horizontal = 34.dp,
                                            vertical = 5.dp
                                        ),
                                        text = stringResource(R.string.add),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Normal,
                                        )
                                    )
                                }

                            }

                        }
                        Row (
                            modifier = Modifier.padding(horizontal = 20.dp)
                        ) {
                            CustomButtonFieldLoad(
                                modifier = Modifier
                                    .width(50.dp)
                                    .padding(start = 5.dp),
                                buttonName = if (detailTraining?.isPublish == false) stringResource(R.string.publish) else stringResource(
                                    R.string.unpublish
                                ),
                                buttonColor = if (detailTraining?.isPublish == false) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                isLoading = state.isLoading
                            ) {
                                detailTrainingViewModel.setPublishedTraining(
                                    isPublish = detailTraining?.isPublish == false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
