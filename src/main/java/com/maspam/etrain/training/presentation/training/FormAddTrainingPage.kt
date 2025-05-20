package com.maspam.etrain.training.presentation.training

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.ConfirmationBottomSheetFlexComponent
import com.maspam.etrain.training.core.presentation.component.CustomButtonField
import com.maspam.etrain.training.core.presentation.component.CustomTextField
import com.maspam.etrain.training.core.presentation.component.DatePickerField
import com.maspam.etrain.training.core.presentation.component.LoadingScreen
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.convertMillisToDate
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.data.dto.body.TrainingBody
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.event.FormTrainingEvent
import com.maspam.etrain.training.presentation.training.viewmodel.TrainingViewModel
import com.mohamedrejeb.richeditor.model.rememberRichTextState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormAddTrainingPage(
    modifier: Modifier = Modifier,
    trainingViewModel: TrainingViewModel,
    onBackPressed: () -> Unit
) {

    val state by trainingViewModel.state.collectAsStateWithLifecycle()
    var datePickerIsOpen by remember { mutableStateOf(false) }
    var isDueDate by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val sheetState = rememberModalBottomSheetState()
    val richState = rememberRichTextState()
    val titleSize = MaterialTheme.typography.displaySmall.fontSize
    val subtitleSize = MaterialTheme.typography.titleLarge.fontSize
    val context = LocalContext.current

    val filename: String? = state.img.path?.substring((state.img.path?.lastIndexOf("/")?.plus(1)!!))

    val singlePickPicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { result ->
        result?.let {
            trainingViewModel.onChangeAction(
                action = FormTrainingEvent.ImgChange(it)
            )
        }
    }

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
            navigateToLoginPage = {},
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

    if (state.isSuccess) {
        SuccessDialog(
            message = "Training successfully added"
        ) {
            onBackPressed()
        }
    }

    Scaffold(
        modifier = modifier
            .systemBarsPadding()
            .fillMaxSize(),
        topBar = {
            TopBarWithArrowComponent(
                section = stringResource(R.string.add_training)
            ) {
                onBackPressed()
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn {
                item {
                    Column {
                        CustomTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp, start = 20.dp, end = 20.dp),
                            valueInput = state.name,
                            readOnly = false,
                            label = stringResource(R.string.name_training),
                            errorMessage = state.nameError,
                            hint = stringResource(R.string.name_training)
                        ) {
                            trainingViewModel.onChangeAction(action = FormTrainingEvent.NameChange(it))
                        }
                        DatePickerField(
                            label = stringResource(R.string.due_date),
                            value = convertMillisToDate(state.dateLine),
                            onCLick = {
                                datePickerIsOpen = true
                                isDueDate = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                        )
                        DatePickerField(
                            label = stringResource(R.string.attendance),
                            value = convertMillisToDate(state.attend),
                            onCLick = {
                                datePickerIsOpen = true
                                isDueDate = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    singlePickPicture.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                }
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp),
                                text = if (filename?.isNotBlank() == true) {
                                    filename
                                } else {
                                    stringResource(R.string.choose_image)
                                },
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }

                        if (state.uriError.isNotBlank()) {
                            Text(
                                text = state.uriError,
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                            )
                        }

                        CustomTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp, start = 20.dp, end = 20.dp),
                            valueInput = state.desc,
                            singleLine = false,
                            readOnly = false,
                            label = stringResource(R.string.description),
                            errorMessage = state.descError,
                            hint = stringResource(R.string.input_description_of_training)
                        ) {
                            trainingViewModel.onChangeAction(action = FormTrainingEvent.DescriptionChange(it))
                        }
                        Spacer(modifier = Modifier.height(110.dp))

                        if (datePickerIsOpen) {
                            DatePickerDialog(
                                colors = DatePickerDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                ),
                                onDismissRequest = {
                                    datePickerIsOpen = false
                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        if (isDueDate) {
                                            trainingViewModel.onChangeAction(
                                                action = FormTrainingEvent.DatelineChange(
                                                    datePickerState.selectedDateMillis ?: 0L
                                                )
                                            )
                                        } else {
                                            trainingViewModel.onChangeAction(
                                                action = FormTrainingEvent.AttendChange(
                                                    datePickerState.selectedDateMillis ?: 0L
                                                )
                                            )
                                        }
                                        datePickerIsOpen = !datePickerIsOpen
                                    }) {
                                        Text("OK")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = {
                                        datePickerIsOpen = !datePickerIsOpen

                                    }) {
                                        Text("Cancel")
                                    }
                                }
                            ) {
                                DatePicker(
                                    state = datePickerState,
                                    colors = DatePickerDefaults.colors(
                                        selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedDayContentColor = Color.White,
                                        selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedYearContentColor = Color.White,
                                        todayContentColor = MaterialTheme.colorScheme.primary,
                                        todayDateBorderColor = MaterialTheme.colorScheme.outline
                                    )
                                )
                            }
                        }
                    }

                }
            }
            CustomButtonField(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 30.dp),
                buttonName = stringResource(R.string.submit),
                buttonColor = MaterialTheme.colorScheme.primary
            ) {
                trainingViewModel.showConfirmationModal(value = true)
            }
            if (state.showConfirmationModal) {
                ModalBottomSheet(
                    onDismissRequest = {
                        trainingViewModel.showConfirmationModal(value = false)
                    },
                    sheetState = sheetState,
                ) {
                    ConfirmationBottomSheetFlexComponent(
                        message = stringResource(R.string.are_you_sure_the_data_is_valid),
                        isLoading = state.isLoading ?: false,
                        onDismiss = {
                            trainingViewModel.showConfirmationModal(false)
                        }
                    ) {
                        trainingViewModel.showConfirmationModal(false)
                        trainingViewModel.onChangeAction(
                            action = FormTrainingEvent.Submit(
                                trainingBody = TrainingBody(
                                    name = state.name,
                                    desc = state.desc,
                                    img = state.img,
                                    attend = state.attend,
                                    dateline = state.dateLine
                                )
                            )
                        )

                    }
                }
            }
            if (state.isLoading == true) {
                LoadingScreen()
            }
        }


    }
}

