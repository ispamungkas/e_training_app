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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.ShimmerEffect
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.domain.model.PostTestModel
import com.maspam.etrain.training.domain.model.SectionModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.viewmodel.PostTestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPostTestPage(
    postTestViewModel: PostTestViewModel,
    postTests: List<PostTestModel>,
    trainingId: Int,
    section: List<SectionModel>?,
    modifier: Modifier = Modifier,
    navigateToLoginPage: () -> Unit,
    navigateToAddPostTestPage: (SectionModel, Int, PostTestModel) -> Unit,
    onBackPressed: () -> Unit
) {

    LaunchedEffect(true) {
        postTestViewModel.apply {
            getTrainingById(trainingId)
        }
    }

    val state by postTestViewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    eventListener(
        postTestViewModel.globalEvent
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                postTestViewModel.setError(event.e)
            }
        }
    }

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                postTestViewModel.getTrainingById(trainingId)
            }
        ) {
            postTestViewModel.setError(e = null)
        }
    }

    if (state.isSuccessful) {
        SuccessDialog(
            message = stringResource(R.string.post_test_content)
        ) {
            postTestViewModel.dismissAlert()
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
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefresh,
            onRefresh = {
                postTestViewModel.run {
                    refresh(trainingId)
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                if (state.data?.sections?.isEmpty() == true) {
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
                    items(items = state.data?.sections ?: emptyList(), key = { it.id ?: 0 }) { data ->
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
                                        navigateToAddPostTestPage(
                                            data,
                                            trainingId,
                                            postTests.find { it.section == data.id } ?: PostTestModel()
                                        )
                                    },
                            ) {
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
                                    Box(modifier = Modifier.background(color = if (state.data?.postTest?.find { it.section == data.id } != null) {
                                        Color.Green.copy(alpha = 0.3f)
                                    } else {
                                        Color.Red.copy(alpha = 0.3f)
                                    }, shape = RoundedCornerShape(12.dp))) {
                                        Text(
                                            text = if (state.data?.postTest?.find { it.section == data.id } != null) {
                                                stringResource(R.string.completed)
                                            } else {
                                                stringResource(R.string.empty)
                                            },
                                            color = if (state.data?.postTest?.find { it.section == data.id } != null) {
                                                Color.Green
                                            } else {
                                                Color.Red
                                            },
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Normal
                                            ),
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(
                                                vertical = 5.dp,
                                                horizontal = 10.dp
                                            )
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

//            if (state.showConfirmation) {
//                ModalBottomSheet(
//                    onDismissRequest = {
//                        listSectionViewModel.setShowConfirmation(value = false)
//                    },
//                    sheetState = sheetState,
//                ) {
//                    ConfirmationBottomSheetFlexComponent(
//                        message = stringResource(R.string.are_you_sure_delete_this_data),
//                        isLoading = state.isLoading,
//                        onDismiss = {
//                            listSectionViewModel.setShowConfirmation(true)
//                        }
//                    ) {
//                        listSectionViewModel.deleteSection(state.selectedSection ?: 0)
//                    }
//                }
//            }
//
//            if (state.showModal) {
//                if (state.isEdit) {
//                    ModalBottomSheet(
//                        onDismissRequest = {
//                            listSectionViewModel.setShowModal(value = false)
//                        },
//                        sheetState = sheetState,
//                    ) {
//                        CustomBottomSheetSectionComponent(
//                            message = stringResource(R.string.please_input_the_relevant_section_in_this_training),
//                            isLoading = state.isLoading,
//                            initialName = state.initialName,
//                            initialJp = state.initialJp,
//                            label = stringResource(R.string.section_name),
//                            onDismiss = {
//                                listSectionViewModel.setShowModal(false)
//                            }
//                        ) { sectionName, jp ->
//                            listSectionViewModel.setShowModal(false)
//                            listSectionViewModel.updateSection(
//                                sectionName = sectionName,
//                                jp = jp,
//                                sectionId = state.selectedSection ?: 0
//                            )
//                        }
//                    }
//                } else {
//                    ModalBottomSheet(
//                        onDismissRequest = {
//                            listSectionViewModel.setShowModal(value = false)
//                        },
//                        sheetState = sheetState,
//                    ) {
//                        CustomBottomSheetSectionComponent(
//                            message = stringResource(R.string.please_input_the_relevant_section_in_this_training),
//                            isLoading = state.isLoading,
//                            label = stringResource(R.string.section_name),
//                            onDismiss = {
//                                listSectionViewModel.setShowModal(false)
//                            }
//                        ) { sectionName, jp ->
//                            listSectionViewModel.setShowModal(false)
//                            listSectionViewModel.addSection(
//                                sectionName = sectionName,
//                                jp = jp,
//                                trainingId = trainingId
//                            )
//                        }
//                    }
//                }
//            }
        }
    }
}