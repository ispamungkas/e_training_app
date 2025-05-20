package com.maspam.etrain.training.presentation.training

import android.net.Uri
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Trash
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.ConfirmationBottomSheetFlexComponent
import com.maspam.etrain.training.core.presentation.component.CustomBottomSheetSectionComponent2
import com.maspam.etrain.training.core.presentation.component.ShimmerEffect
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.domain.model.TopicModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.viewmodel.ListTopicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTopicPage(
    listTopicViewModel: ListTopicViewModel,
    topicList: List<TopicModel>?,
    sectionId: Int,
    modifier: Modifier = Modifier,
    navigateToLoginPage: () -> Unit,
    navigateToTopicUpdateScreen: (TopicModel) -> Unit,
    onBackPressed: () -> Unit
) {

    LaunchedEffect(true) {
        listTopicViewModel.apply {
            setInitialValue(topicList ?: emptyList(), sectionId)
            getAllTopic()
        }
    }

    val state by listTopicViewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()

    eventListener(
        listTopicViewModel.globalEvent
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                listTopicViewModel.setError(event.e)
            }
        }
    }

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                listTopicViewModel.getAllTopic()
            }
        ) {
            listTopicViewModel.setError(e = null)
        }
    }


    if (state.isSuccess) {
        SuccessDialog(
            message = stringResource(R.string.topic_has_been_added)
        ) {
            listTopicViewModel.dismissAlert()
        }
    }

    Scaffold(
        modifier = modifier
            .systemBarsPadding()
            .fillMaxSize(),
        topBar = {
            TopBarWithArrowComponent(
                section = stringResource(R.string.topic_section),
                onBackPress = onBackPressed
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable {
                        listTopicViewModel.setShowModal(true)
                    }
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .align(alignment = Alignment.Center),
                    imageVector = Lucide.Plus,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Icon Add"
                )
            }
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefresh,
            onRefresh = {
                listTopicViewModel.run {
                    refresh()
                    getAllTopic()
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {

                if (state.isLoading) {
                    items(count = 5) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                        ) {
                            ShimmerEffect(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                if (state.data?.isEmpty() == true) {
                    item {
                        Text(
                            text = stringResource(R.string.doesn_t_topic_on_this_section),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(items = state.data ?: emptyList(), key = { it.id ?: 0 }) { data ->
                        if (state.isLoading) {
                            for (i in 0..3) {
                                Column(
                                    modifier = Modifier
                                        .padding(20.dp)
                                ) {
                                    ShimmerEffect(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(30.dp)
                                    )
                                    Spacer(modifier = Modifier.height(15.dp))
                                    HorizontalDivider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                                    .clickable {
                                        navigateToTopicUpdateScreen(data)
                                    }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                        .background(
                                            color =
                                            if (data.content?.isNotBlank() == true) Color.Green.copy(
                                                alpha = 0.3f
                                            ) else MaterialTheme.colorScheme.error.copy(
                                                0.3f
                                            ),
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .clip(shape = RoundedCornerShape(15.dp))
                                        .align(alignment = Alignment.End)
                                ) {
                                    Text(
                                        if (data.content?.isNotBlank() == true) stringResource(R.string.completed) else stringResource(
                                            R.string.un_completed
                                        ),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = if (data.content?.isNotBlank() == true) Color.Green else Color.Red
                                        ),
                                        modifier = Modifier.padding(
                                            horizontal = 15.dp,
                                            vertical = 6.dp
                                        )
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(shape = RoundedCornerShape(15.dp))
                                        .padding(vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = data.name ?: stringResource(R.string.section_name),
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(start = 15.dp)
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                                    ) {
                                        Icon(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clickable {
                                                    listTopicViewModel.run {
                                                        setShowConfirmation(true)
                                                        setTopicId(data.id ?: 0)
                                                    }
                                                },
                                            tint = MaterialTheme.colorScheme.primary,
                                            imageVector = Lucide.Trash,
                                            contentDescription = "Icon Trash"
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                                HorizontalDivider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }

                        }
                    }
                }


            }

            if (state.showConfirmation) {
                ModalBottomSheet(
                    onDismissRequest = {
                        listTopicViewModel.setShowConfirmation(value = false)
                    },
                    sheetState = sheetState,
                ) {
                    ConfirmationBottomSheetFlexComponent(
                        message = stringResource(R.string.please_input_the_relevant_topic_in_this_training),
                        isLoading = state.isLoading,
                        onDismiss = {
                            listTopicViewModel.setShowConfirmation(true)
                        }
                    ) {
                        listTopicViewModel.deleteSection(state.topicId ?: 0)
                    }
                }

            }

            if (state.showModal) {
                ModalBottomSheet(
                    onDismissRequest = {
                        listTopicViewModel.setShowModal(value = false)
                    },
                    sheetState = sheetState,
                ) {
                    CustomBottomSheetSectionComponent2(
                        message = stringResource(R.string.are_you_sure_delete_this_data),
                        isLoading = state.isLoading,
                        label = stringResource(R.string.section_name),
                        onDismiss = {
                            listTopicViewModel.setShowModal(false)
                        }
                    ) { topicName ->
                        listTopicViewModel.setShowModal(false)
                        listTopicViewModel.setTopic(
                            topicName = topicName,
                            img = Uri.parse(""),
                            content = "",
                            sectionId = sectionId,
                        )
                    }
                }
            }
        }
    }
}