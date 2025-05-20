package com.maspam.etrain.training.presentation.dashboard

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.LogOut
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.UserPen
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.Menu
import com.maspam.etrain.training.domain.model.NewsModel
import com.maspam.etrain.training.presentation.dashboard.component.ImageSliderComponent
import com.maspam.etrain.training.presentation.dashboard.component.LoadingComponent
import com.maspam.etrain.training.presentation.dashboard.component.MenusComponent
import com.maspam.etrain.training.presentation.dashboard.component.NewsItemComponent
import com.maspam.etrain.training.presentation.dashboard.component.OpenTrainingComponent
import com.maspam.etrain.training.presentation.dashboard.component.ScannerButtonComponent
import com.maspam.etrain.training.presentation.dashboard.viewmodel.DashboardViewModel
import com.maspam.etrain.training.presentation.profile.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperUserDashboardPage(
    dashboardViewModel: DashboardViewModel,
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier,
    navigateToDetailNews: (NewsModel) -> Unit,
    navigateToListNews: () -> Unit,
    navigateToScannerPage: () -> Unit,
    navigateToLoginPage:() -> Unit,
    navigateToListUserAccountPage: () -> Unit,
) {
    val state by dashboardViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "")
                },
                actions = {
                    Icon(imageVector = Lucide.LogOut, contentDescription = "", modifier = Modifier.clickable{
                        profileViewModel.removeSession()
                        navigateToLoginPage()
                    }
                        , tint = MaterialTheme.colorScheme.primary)
                }
            )
        }
    ) { innerPadding ->

        val context = LocalContext.current

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
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                        ) {
                            Text(
                                stringResource(R.string.feature),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(start = 20.dp, top = 10.dp)
//                                    .clickable {
//                                    profileViewModel.removeSession()
//                                    navigateToLoginPage()
//                                }
                            )
                            ImageSliderComponent(
                                modifier = Modifier.padding(vertical = 20.dp)
                            )
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp),
                                text = stringResource(R.string.menu),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal)
                            )
                            MenusComponent(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 15.dp),
                                menus = listOf(
                                    Menu(name = stringResource(R.string.account), imageVector = Lucide.UserPen)
                                )
                            ) {
                                when (it) {
                                    0 -> navigateToListUserAccountPage()
                                }
                            }
                            OpenTrainingComponent(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                dataTraining = state.openTrain ?: emptyList(),
                                isLoading = state.isLoading,
                                onClickItem = { id ->
                                    Toast.makeText(context, "You are super user", Toast.LENGTH_SHORT).show()
                                },
                                navigateToListOpenTraining = {}
                            )
                        }
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
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 5.dp)
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

                    item { Spacer(modifier = Modifier.height(75.dp)) }
                }
                ScannerButtonComponent(
                    modifier = Modifier.align(alignment = Alignment.BottomCenter)
                ) {
                    navigateToScannerPage()
                }
            }
        }
    }
}

