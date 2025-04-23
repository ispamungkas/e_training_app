package com.maspam.etrain.training.presentation.training

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.presentation.dashboard.component.LoadingComponent
import com.maspam.etrain.training.presentation.dashboard.component.OpenTrainingItemComponent
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.viewmodel.TrainingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListTrainingPage(
    trainingViewModel: TrainingViewModel,
    modifier: Modifier = Modifier,
    navigateToLoginPage: () -> Unit,
    onDetailTraining: () -> Unit,
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
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable {

                    }
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(20.dp),
                    imageVector = Lucide.Plus,
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
                    .padding(innerPadding)
            ) {

                item {
                    FilterListComponent(
                        filters = listOf(
                            "All", "Close", "Open", "Publish"
                        ),
                        onSelected = { filter ->
                            when (filter) {
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
                            .fillMaxSize(),
                        valueInput = state.search,
                        readOnly = false,
                        label = "Search",
                        hint = "Search",
                    ) { value ->
                        trainingViewModel.setValueOfSearch(value)
                    }
                }

                if (state.isLoading == true) {
                    items(4) {
                        LoadingComponent(size = 100.dp, modifier = Modifier.fillMaxWidth())
                    }
                } else {
                    state.filteredList?.let {
                        items(items = it, key = { x -> x.id ?: 0 }) { training ->
                            OpenTrainingItemComponent(
                                modifier = Modifier.fillMaxWidth(),
                                id = training.id,
                                isOpen = training.isOpen,
                                nameTraining = training.name,
                                typeTraining = training.typeTraining,
                                due = training.due,
                                typeTrainingCategory = training.typeTrainingCategory,
                                imageUri = training.image,
                                totalPerson = training.totalTaken,
                                onItemClick = {
                                    onDetailTraining()
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