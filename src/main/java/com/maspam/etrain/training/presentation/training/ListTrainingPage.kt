package com.maspam.etrain.training.presentation.training

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.CustomTextField
import com.maspam.etrain.training.core.presentation.component.FilterListComponent
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.component.bounceClick
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.domain.model.TrainingModel
import com.maspam.etrain.training.presentation.dashboard.component.LoadingComponent
import com.maspam.etrain.training.presentation.dashboard.component.OpenTrainingItemComponent
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.viewmodel.TrainingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTrainingPage(
    trainingViewModel: TrainingViewModel,
    modifier: Modifier = Modifier,
    navigateToFormAddTraining: () -> Unit,
    navigateToLoginPage: () -> Unit,
    onBackPressed: () -> Unit,
    onDetailTraining: (TrainingModel) -> Unit,
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
            .systemBarsPadding(),
        topBar = {
            TopBarWithArrowComponent(
                section = stringResource(R.string.training)
            ) {
                onBackPressed()
            }
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .bounceClick()
                    .clickable {
                        navigateToFormAddTraining()
                    }
            ) {
                Icon(
                    modifier = Modifier
                        .size(25.dp)
                        .align(alignment = Alignment.Center),
                    imageVector = Lucide.Plus,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Icon Add"
                )
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
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                item {
                    FilterListComponent(
                        modifier = Modifier.padding(top = 10.dp),
                        filters = listOf(
                            "All", "Close", "Open", "Publish"
                        ),
                        onSelected = { filter ->
                            when (filter) {
                                "All" -> trainingViewModel.setFilterAll()
                                "Close" -> trainingViewModel.setFilter(false)
                                "Open" -> trainingViewModel.setFilter(true)
                                "Publish" -> trainingViewModel.setFilterPublish(true)
                                else -> trainingViewModel.setFilter(null)
                            }
                        }
                    )
                }
                item {
                    CustomTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        valueInput = state.search,
                        readOnly = false,
                        label = stringResource(R.string.search),
                        hint = stringResource(R.string.search),
                    ) { value ->
                        trainingViewModel.setValueOfSearch(value)
                    }
                }

                if (state.isLoading == true) {
                    items(4) {
                        LoadingComponent(size = 100.dp, modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp))
                    }
                } else {
                    state.filteredList?.let {
                        items(items = it, key = { x -> x.id ?: 0 }) { training ->
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
                                    onDetailTraining(training)
                                }
                            )
                        }

                    } ?: item {
                        Text(
                            text = stringResource(R.string.no_training_has_open),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}