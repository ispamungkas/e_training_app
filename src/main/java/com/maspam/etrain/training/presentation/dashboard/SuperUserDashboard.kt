package com.maspam.etrain.training.presentation.dashboard

import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Brain
import com.composables.icons.lucide.Lucide
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.Menu
import com.maspam.etrain.training.presentation.dashboard.component.ImageSliderComponent
import com.maspam.etrain.training.presentation.dashboard.component.MenusComponent
import com.maspam.etrain.training.presentation.dashboard.component.OpenTrainingComponent
import com.maspam.etrain.training.presentation.dashboard.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperUserDashboardPage(
    scrollState: ScrollState = rememberScrollState(initial = 0),
    dashboardViewModel: DashboardViewModel,
    modifier: Modifier = Modifier
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val state by dashboardViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier
    ) { innerPadding ->

        val pullToRefreshState = rememberPullToRefreshState()
        val context = LocalContext.current

//        if (pullToRefreshState.isRefreshing) {
//            LaunchedEffect(true) {
//                isRefreshing = true
//            }
//        }
//
//        LaunchedEffect(
//            isRefreshing
//        ) {
//            if (isRefreshing) {
//                pullToRefreshState.startRefresh()
//            } else {
//                pullToRefreshState.endRefresh()
//                isRefreshing = false
//            }
//        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .scrollable(state = scrollState, orientation = Orientation.Vertical)
        ) {
            Text(
                stringResource(R.string.feature),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(start = 20.dp, top = 10.dp)
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
                    Menu(name = stringResource(R.string.training), imageVector = Lucide.Brain)
                )
            )
            OpenTrainingComponent(
                dataTraining = state.openTrain ?: emptyList(),
                isLoading = state.isLoading,
                onClickItem = { id ->
                    Toast.makeText(context, "Under development", Toast.LENGTH_SHORT).show()
                },
                navigateToListOpenTraining = {}
            )
        }
    }
}

