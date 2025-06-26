package com.maspam.etrain.training.presentation.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Brain
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Newspaper
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.Menu
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.TopBarComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.domain.model.NewsModel
import com.maspam.etrain.training.presentation.dashboard.component.ImageSliderComponent
import com.maspam.etrain.training.presentation.dashboard.component.LoadingComponent
import com.maspam.etrain.training.presentation.dashboard.component.MenusComponent
import com.maspam.etrain.training.presentation.dashboard.component.ModalTrainingDetailComponent
import com.maspam.etrain.training.presentation.dashboard.component.NewsItemComponent
import com.maspam.etrain.training.presentation.dashboard.component.OpenTrainingComponent
import com.maspam.etrain.training.presentation.dashboard.component.ScannerButtonComponent
import com.maspam.etrain.training.presentation.dashboard.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KabidDashboardPage(
    dashboardViewModel: DashboardViewModel,
    modifier: Modifier = Modifier,
    navigateToEnrollList: () -> Unit,
    navigateToListOpenTraining: () -> Unit,
    onProfileClicked: () -> Unit,
    navigateToListNews: () -> Unit,
    navigateToDetailNews: (NewsModel) -> Unit,
    navigateToListTrainingManajement: () -> Unit,
    navigateToKabidListNews: () -> Unit,
    navigateToScannerPage: () -> Unit,
    navigateToLoginPage: () -> Unit
) {
    val state by dashboardViewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                dashboardViewModel.apply {
                    setError(e = null)
                    getNews()
                    getOpenTraining()
                }
            },
            onDismiss = { dashboardViewModel.setError(e = null) },
        )
    }

    if (state.isSuccessful) {
        SuccessDialog(
            message = stringResource(R.string.successfully_enroll_training)
        ) {
            dashboardViewModel.dismisDialog()
            navigateToEnrollList()
        }
    }

    if (state.isRefresh) {
        dashboardViewModel.run {
            getNews()
            getOpenTraining()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            TopBarComponent(
                imageUri = state.user?.image,
                name = state.user?.name ?: stringResource(R.string.username),
            ) {
                onProfileClicked()
            }
        }
    ) { innerPadding ->

        PullToRefreshBox(
            isRefreshing = state.isRefresh,
            onRefresh = {
                dashboardViewModel.apply {
                    refresh()
                    getUser()
                    getNews()
                    getOpenTraining()
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize())
            {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    item {
                        Text(
                            stringResource(R.string.feature),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(start = 20.dp, top = 10.dp)
                        )
                    }

                    item {
                        ImageSliderComponent(
                            modifier = Modifier.padding(vertical = 20.dp)
                        )
                    }

                    item {
                        Text(
                            modifier = Modifier
                                .padding(start = 20.dp, bottom = 10.dp, end = 20.dp),
                            text = stringResource(R.string.menu),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
                        )
                    }

                    item {
                        MenusComponent(
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 15.dp),
                            menus = listOf(
                                Menu(
                                    name = stringResource(R.string.training),
                                    imageVector = Lucide.Brain
                                ),
                                Menu(
                                    name = stringResource(R.string.news),
                                    imageVector = Lucide.Newspaper
                                ),
                            )
                        ) { index ->
                            when (index) {
                                0 -> navigateToListTrainingManajement()
                                1 -> navigateToKabidListNews()
                            }

                        }
                    }

                    item {
                        OpenTrainingComponent(
                            currentTraining = state.user?.enroll ?: emptyList(),
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                            dataTraining = state.openTrain ?: emptyList(),
                            isLoading = state.isLoading,
                            onClickItem = { id ->
                                dashboardViewModel.setModalBottomSheetState(value = true, id = id)
                            },
                            navigateToListOpenTraining = {
                                navigateToListOpenTraining()
                            },
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(R.string.news),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                            )
                            Text(
                                stringResource(R.string.view_all),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier
                                    .clickable {
                                        navigateToListNews()
                                    }
                            )
                        }
                    }

                    if (state.isLoading) {
                        items(4) {
                            LoadingComponent(
                                size = 110.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 5.dp)
                            )
                        }
                    } else {
                        state.news?.let { news ->
                            items(news) { newsItem ->
                                NewsItemComponent(
                                    image = newsItem.image,
                                    newsHeadline = newsItem.desc,
                                    author = newsItem.author,
                                    postDate = newsItem.publishDate,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .clickable {
                                            navigateToDetailNews(newsItem)
                                        }
                                )
                            }
                        } ?: item {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.no_news_for_today),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal)
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(75.dp)) }
                }
//                ScannerButtonComponent(
//                    modifier = Modifier.align(alignment = Alignment.BottomCenter)
//                ) {
//                    navigateToScannerPage()
//                }
            }
            if (state.isBottomSheetShow == true) {
                ModalBottomSheet(
                    onDismissRequest = {
                        dashboardViewModel.setModalBottomSheetState(value = false)
                    },
                    sheetState = sheetState,
                ) {
                    val data = state.selectedTrain
                    ModalTrainingDetailComponent(
                        modifier = Modifier.padding(
                            horizontal = 20.dp
                        ),
                        image = data?.image,
                        typeTraining = data?.typeTraining,
                        due = data?.due,
                        isOpen = data?.isOpen,
                        trainingName = data?.name,
                        typeTrainingAtt = data?.typeTrainingCategory,
                        totalTaken = data?.totalTaken,
                        totalJP = data?.totalJp,
                        description = data?.desc,
                        isLoading = state.isLoading,
                        onCancel = { dashboardViewModel.setModalBottomSheetState(value = false) },
                        onSubmit = {
                            data?.id?.let {
                                dashboardViewModel.enrollTraining(trainingId = it)
                            }

                        },
                    )
                }
            }
        }
    }
}