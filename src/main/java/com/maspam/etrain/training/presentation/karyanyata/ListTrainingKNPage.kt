package com.maspam.etrain.training.presentation.karyanyata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.domain.model.TrainingModel
import com.maspam.etrain.training.presentation.dashboard.component.LoadingComponent
import com.maspam.etrain.training.presentation.dashboard.component.OpenTrainingItemComponent
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.viewmodel.TrainingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTrainingKNPage(
    modifier: Modifier = Modifier,
    trainingViewModel: TrainingViewModel,
    navigateToLoginPage: () -> Unit,
    navigateToDetailTrainingKN: (TrainingModel) -> Unit,
    onBackPressed: () -> Unit,
) {

    val state by trainingViewModel.state.collectAsStateWithLifecycle()

    eventListener(
        trainingViewModel.globalEvent
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                trainingViewModel.setError(event.e)
            }
        }
    }

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                trainingViewModel.apply {
                    setError(e = null)
                    getListTraining()
                }
            }
        ) {
            trainingViewModel.setError(e = null)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            TopBarWithArrowComponent(
                section = stringResource(R.string.karya_nyata_section)
            ){
                onBackPressed()
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefresh,
            onRefresh = {
                trainingViewModel.run {
                    refresh()
                    getListTraining()
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                if (state.isLoading == true) {
                    items(4) {
                        LoadingComponent(
                            size = 100.dp, modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                        )
                    }
                } else {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    items(state.listTraining ?: emptyList(), key = { it.id ?: 0 }) { training ->
                        OpenTrainingItemComponent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            id = training.id,
                            isOpen = training.isOpen,
                            nameTraining = training.name,
                            typeTraining = training.typeTraining,
                            due = training.due,
                            typeTrainingCategory = training.typeTrainingCategory,
                            imageUri = training.image,
                            totalPerson = training.totalTaken,
                            onItemClick = {
                                navigateToDetailTrainingKN(training)
                            }
                        )
                    }
                }

            }
        }
    }
}