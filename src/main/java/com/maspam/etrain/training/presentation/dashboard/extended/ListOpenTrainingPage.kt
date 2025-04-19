package com.maspam.etrain.training.presentation.dashboard.extended

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.presentation.dashboard.component.LoadingComponent
import com.maspam.etrain.training.presentation.dashboard.component.ModalTrainingDetailComponent
import com.maspam.etrain.training.presentation.dashboard.component.OpenTrainingItemComponent
import com.maspam.etrain.training.presentation.dashboard.viewmodel.ListOpenTrainingViewModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOpenTrainingPage(
    listOpenTrainingViewModel: ListOpenTrainingViewModel,
    onBackPressed: () -> Unit,
    navigateToLoginPage: () -> Unit,
    navigateToEnrollList: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by listOpenTrainingViewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()

    eventListener(
        listOpenTrainingViewModel.event
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                listOpenTrainingViewModel.setError(event.e)
            }
        }
    }

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                listOpenTrainingViewModel.apply {
                    setError(e = null)
                    getOpenTraining()
                }
            },
            onDismiss = { listOpenTrainingViewModel.setError(e = null) },
        )
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopBarWithArrowComponent(
                section = "Open Training",
                onBackPress = onBackPressed
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(top = 20.dp, bottom = 30.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(innerPadding)
        ) {
            if (state.isLoading) {
                items(4) {
                    LoadingComponent(size = 100.dp, modifier = Modifier.fillMaxWidth())
                }
            } else {
                state.data?.let {
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
                               listOpenTrainingViewModel.setModalBottomSheetState(value = true, it)
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

        if (state.isBottomSheetShow) {
            ModalBottomSheet(
                onDismissRequest = {
                    listOpenTrainingViewModel.setModalBottomSheetState(value = false)
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
                    onCancel = { listOpenTrainingViewModel.setModalBottomSheetState(value = false) },
                    onSubmit = {
                        data?.id?.let {
                            listOpenTrainingViewModel.enrollTraining(trainingId = it)
                        }
                        navigateToEnrollList()
                    },
                )
            }
        }
    }
}