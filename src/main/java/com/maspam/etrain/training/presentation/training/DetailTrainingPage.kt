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
import androidx.compose.foundation.layout.systemBarsPadding
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
import com.composables.icons.lucide.Pen
import com.composables.icons.lucide.TimerReset
import com.maspam.etrain.R
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.presentation.component.CustomButtonFieldLoad
import com.maspam.etrain.training.core.presentation.utils.ExpandableText
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.convertMillisToDate
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.domain.model.PostTestModel
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
    navigateToEditPage: (TrainingModel) -> Unit,
    navigateToListSection: (List<SectionModel>, Int) -> Unit,
    navigateToPostTestPage: (List<SectionModel>, Int, List<PostTestModel>) -> Unit,
) {

    LaunchedEffect(true) {
        detailTrainingViewModel.setInitialValue(training)
        detailTrainingViewModel.getTrainingById()
    }

    val state by detailTrainingViewModel.state.collectAsStateWithLifecycle()
    val detailTraining = state.data

    val sectionId = detailTraining?.sections?.map { it.id }
    val postTestSize = sectionId?.mapNotNull {
        detailTraining.postTest?.find { post -> post.section == it }
    }

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
            .fillMaxSize()
            .systemBarsPadding(),
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
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentScale = ContentScale.Crop,
                                model = constructUrl(detailTraining?.image ?: ""),
                                error = painterResource(R.drawable.placeholder_image_sample),
                                placeholder = painterResource(R.drawable.placeholder_image_sample),
                                contentDescription = "Training Image"
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 15.dp, end = 20.dp, start = 20.dp)
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
                                        text = convertMillisToDate(detailTraining?.due ?: 0L),
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal),

                                        )
                                }
                            }

                            Text(
                                modifier = Modifier.padding(vertical = 15.dp, horizontal = 20.dp),
                                text = detailTraining?.name
                                    ?: stringResource(R.string.name_training),
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
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                                text = stringResource(R.string.materi),
                                overflow = TextOverflow.Clip,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
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
                                            color = if ((detailTraining?.sections?.size
                                                    ?: 0) >= 1
                                            ) {
                                                Color.Green
                                            } else {
                                                Color.Red
                                            }
                                        )
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.outline,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable {
                                            navigateToListSection(
                                                detailTraining?.sections ?: emptyList(),
                                                training.id ?: 0
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

                            Text(
                                modifier = Modifier.padding(
                                    top = 30.dp,
                                    start = 20.dp,
                                    end = 20.dp
                                ),
                                text = stringResource(R.string.post_test),
                                overflow = TextOverflow.Clip,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = if ((detailTraining?.postTest?.size
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
                                        text = if ((detailTraining?.postTest?.size
                                                ?: 0) >= 1
                                        ) {
                                            stringResource(R.string.completed)
                                        } else {
                                            stringResource(R.string.empty)
                                        },
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Normal,
                                            color = if ((detailTraining?.postTest?.size
                                                    ?: 0) >= 1) {
                                                Color.Green
                                            } else {
                                                Color.Red
                                            }
                                        )
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.outline,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable {
                                            navigateToPostTestPage(
                                                detailTraining?.sections ?: emptyList(),
                                                training.id ?: 0,
                                                postTestSize ?: emptyList()
                                            )
                                        }
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .padding(
                                                horizontal = 34.dp,
                                                vertical = 5.dp
                                            )
                                            .clip(shape = RoundedCornerShape(10.dp)),
                                        text = stringResource(R.string.add),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Normal,
                                        )
                                    )
                                }

                            }

                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .align(alignment = Alignment.BottomCenter)
                        .fillMaxWidth()
                ) {
                    CustomButtonFieldLoad(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 30.dp),
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
                Box(
                    modifier = Modifier
                        .padding(top = 40.dp, end = 30.dp)
                        .align(alignment = Alignment.TopEnd)
                        .size(50.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clip(shape = RoundedCornerShape(20.dp))
                        .clickable {
                            navigateToEditPage(detailTraining ?: TrainingModel())
                        }
                ) {
                    Icon(
                        imageVector = Lucide.Pen,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }
            }
        }
    }
}
