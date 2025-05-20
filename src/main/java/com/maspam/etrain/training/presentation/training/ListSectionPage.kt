package com.maspam.etrain.training.presentation.training

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
import com.composables.icons.lucide.Pen
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Trash
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.ConfirmationBottomSheetFlexComponent
import com.maspam.etrain.training.core.presentation.component.CustomBottomSheetSectionComponent
import com.maspam.etrain.training.core.presentation.component.ShimmerEffect
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.domain.model.SectionModel
import com.maspam.etrain.training.domain.model.TopicModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.viewmodel.ListSectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSectionPage(
    listSectionViewModel: ListSectionViewModel,
    trainingId: Int,
    section: List<SectionModel>?,
    modifier: Modifier = Modifier,
    navigateToLoginPage: () -> Unit,
    navigateToTopicPage: (List<TopicModel>, Int) -> Unit,
    onBackPressed: () -> Unit
) {

    LaunchedEffect(true) {
        listSectionViewModel.apply {
            setInitialValue(section ?: emptyList(), trainingId = trainingId)
            getAllSection()
        }
    }

    val state by listSectionViewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()

    eventListener(
        listSectionViewModel.globalEvent
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                listSectionViewModel.setError(event.e)
            }
        }
    }

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                listSectionViewModel.getAllSection()
            }
        ) {
            listSectionViewModel.setError(e = null)
        }
    }

    if (state.isSuccess) {
        SuccessDialog(
            message = stringResource(R.string.section_berhasil_terupdate)
        ) {
            listSectionViewModel.dismissAlert()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            TopBarWithArrowComponent(
                section = stringResource(R.string.material_section),
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
                        listSectionViewModel.apply {
                            setShowModal(true)
                            setIsAdd()
                        }
                    }
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .align(alignment = Alignment.Center),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    imageVector = Lucide.Plus,
                    contentDescription = "Icon Add"
                )
            }
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefresh,
            onRefresh = {
                listSectionViewModel.run {
                    refresh()
                    getAllSection()
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                if (state.data?.isEmpty() == true) {
                    item {
                        Text(
                            text = stringResource(R.string.doesn_t_section_on_this_training),
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
                                    .clip(shape = RoundedCornerShape(10.dp))
                                    .clickable {
                                        navigateToTopicPage(
                                            data.topics ?: emptyList(),
                                            data.id ?: 0
                                        )
                                    },
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                        .background(
                                            color =
                                            if (data.status == "completed") Color.Green.copy(alpha = 0.3f) else MaterialTheme.colorScheme.error.copy(
                                                0.3f
                                            ),
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .clip(shape = RoundedCornerShape(15.dp))
                                        .align(alignment = Alignment.End)
                                ) {
                                    Text(
                                        data.status ?: "",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = if (data.status == "completed") Color.Green else Color.Red
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
                                                    listSectionViewModel.setInitialNameJpAndSectionId(
                                                        data.name ?: "", data.jp ?: 0, data.id ?: 0
                                                    )
                                                },
                                            imageVector = Lucide.Pen,
                                            tint = MaterialTheme.colorScheme.primary,
                                            contentDescription = "Icon Trash"
                                        )
                                        Icon(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clickable {
                                                    listSectionViewModel.run {
                                                        setSelectedSection(data.id ?: 0)
                                                        setShowConfirmation(true)
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
                        listSectionViewModel.setShowConfirmation(value = false)
                    },
                    sheetState = sheetState,
                ) {
                    ConfirmationBottomSheetFlexComponent(
                        message = stringResource(R.string.are_you_sure_delete_this_data),
                        isLoading = state.isLoading,
                        onDismiss = {
                            listSectionViewModel.setShowConfirmation(true)
                        }
                    ) {
                        listSectionViewModel.deleteSection(state.selectedSection ?: 0)
                    }
                }
            }

            if (state.showModal) {
                if (state.isEdit) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            listSectionViewModel.setShowModal(value = false)
                        },
                        sheetState = sheetState,
                    ) {
                        CustomBottomSheetSectionComponent(
                            message = stringResource(R.string.please_input_the_relevant_section_in_this_training),
                            isLoading = state.isLoading,
                            initialName = state.initialName,
                            initialJp = state.initialJp,
                            label = stringResource(R.string.section_name),
                            onDismiss = {
                                listSectionViewModel.setShowModal(false)
                            }
                        ) { sectionName, jp ->
                            listSectionViewModel.setShowModal(false)
                            listSectionViewModel.updateSection(
                                sectionName = sectionName,
                                jp = jp,
                                sectionId = state.selectedSection ?: 0
                            )
                        }
                    }
                } else {
                    ModalBottomSheet(
                        onDismissRequest = {
                            listSectionViewModel.setShowModal(value = false)
                        },
                        sheetState = sheetState,
                    ) {
                        CustomBottomSheetSectionComponent(
                            message = stringResource(R.string.please_input_the_relevant_section_in_this_training),
                            isLoading = state.isLoading,
                            label = stringResource(R.string.section_name),
                            onDismiss = {
                                listSectionViewModel.setShowModal(false)
                            }
                        ) { sectionName, jp ->
                            listSectionViewModel.setShowModal(false)
                            listSectionViewModel.addSection(
                                sectionName = sectionName,
                                jp = jp,
                                trainingId = trainingId
                            )
                        }
                    }
                }
            }
        }
    }
}