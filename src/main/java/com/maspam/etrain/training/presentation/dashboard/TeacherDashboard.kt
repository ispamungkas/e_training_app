package com.maspam.etrain.training.presentation.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.TopBarComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.domain.model.NewsModel
import com.maspam.etrain.training.presentation.dashboard.component.ImageSliderComponent
import com.maspam.etrain.training.presentation.dashboard.component.LoadingComponent
import com.maspam.etrain.training.presentation.dashboard.component.ModalTrainingDetailComponent
import com.maspam.etrain.training.presentation.dashboard.component.NewsItemComponent
import com.maspam.etrain.training.presentation.dashboard.component.OpenTrainingComponent
import com.maspam.etrain.training.presentation.dashboard.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardPage(
    dashboardViewModel: DashboardViewModel,
    navigateToLoginPage: () -> Unit,
    navigateToEnrollList: () -> Unit,
    onProfileClicked: () -> Unit,
    navigateToListOpenTraining: () -> Unit,
    navigateToListNews: () -> Unit,
    navigateToDetailNews: (NewsModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    var isRefreshing by remember { mutableStateOf(false) }
    val state by dashboardViewModel.state.collectAsStateWithLifecycle()

    state.error?.let {
        println("error $it")
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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBarComponent(
                name = state.user?.name ?: stringResource(R.string.username),
            ) {
                onProfileClicked()
            }
        },
    ) { innerPadding ->
        val pullToRefreshState = rememberPullToRefreshState()

        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) { isRefreshing = true }
        }

        LaunchedEffect(isRefreshing) {
            if (isRefreshing) pullToRefreshState.startRefresh()
            else {
                pullToRefreshState.endRefresh()
                isRefreshing = false
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .nestedScroll(pullToRefreshState.nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    stringResource(R.string.feature),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp)
                )
            }

            item {
                ImageSliderComponent(
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }

            item {
                OpenTrainingComponent(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    dataTraining = state.openTrain ?: emptyList(),
                    isLoading = state.isLoading,
                    onClickItem = { id ->
                        dashboardViewModel.setModalBottomSheetState(value = true, id = id)
                    },
                    navigateToListOpenTraining = navigateToListOpenTraining
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
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
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
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 5.dp)
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

            item { Spacer(modifier = Modifier.height(20.dp)) }
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
                        navigateToEnrollList()
                    },
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showBackground = true,
    device = "id:pixel_5"
)
@Composable
private fun TPrev() {
//    TeacherDashboardPage()
}