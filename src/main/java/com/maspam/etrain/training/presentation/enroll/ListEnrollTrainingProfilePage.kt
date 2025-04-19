package com.maspam.etrain.training.presentation.enroll

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.FilterListComponent
import com.maspam.etrain.training.core.presentation.component.ListEnrollTrainingComponent
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.domain.model.EnrollModel
import com.maspam.etrain.training.presentation.dashboard.component.LoadingComponent
import com.maspam.etrain.training.presentation.enroll.viewmodel.ListEnrollProfileViewModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent

@Composable
fun ListEnrollTrainingProfilePage(
    listEnrollProfileViewModel: ListEnrollProfileViewModel,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    navigateToLoginPage: () -> Unit,
    onItemClick: (EnrollModel) -> Unit
) {

    val state by listEnrollProfileViewModel.state.collectAsState()

    eventListener(
        listEnrollProfileViewModel.globalEvent
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                listEnrollProfileViewModel.setError(event.e)
            }
        }
    }

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                listEnrollProfileViewModel.apply {
                    setError(e = null)
                    getEnroll()
                }
            }
        ) {
            listEnrollProfileViewModel.setError(e = null)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopBarWithArrowComponent(
                section = stringResource(R.string.enroll),
                onBackPress = onBackPressed
            )
        }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 30.dp),
            modifier = Modifier
                .padding(innerPadding)
        ) {
            item {
                FilterListComponent(
                    filters = listOf(
                        "All", "Progress", "Time out", "Completed", "Need action"
                    ),
                    onSelected = { filter ->
                        listEnrollProfileViewModel.setFilter(filter)
                    }
                )
            }

            if (state.isLoading) {
                items(5) {
                    LoadingComponent(
                        size = 100.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                }
            } else {
                state.filteredData?.let {
                    items(it, key = { enrollData -> enrollData.id ?: 0 }) { enrollData ->
                        ListEnrollTrainingComponent(
                            modifier = Modifier.fillMaxWidth(),
                            data = enrollData,
                            id = enrollData.id,
                            typeTraining = enrollData.trainingDetail?.typeTraining,
                            status = enrollData.status,
                            typeTrainingCategory = enrollData.trainingDetail?.typeTrainingCategory,
                            nameTraining = enrollData.trainingDetail?.name,
                            due = enrollData.outDate,
                            imageUri = enrollData.trainingDetail?.image,
                            onItemClick = onItemClick
                        )
                    }
                } ?: item {
                    Text(
                        text = "Doen't have any enroll training",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    )
                }
            }
        }
    }
}
