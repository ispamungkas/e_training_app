package com.maspam.etrain.training.presentation.profile

import android.icu.text.SimpleDateFormat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.CustomButtonFieldLoad
import com.maspam.etrain.training.core.presentation.component.CustomEmailTextField
import com.maspam.etrain.training.core.presentation.component.CustomFormChoice
import com.maspam.etrain.training.core.presentation.component.CustomNumberTextField
import com.maspam.etrain.training.core.presentation.component.CustomTextField
import com.maspam.etrain.training.core.presentation.component.DatePickerField
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.Gender
import com.maspam.etrain.training.core.presentation.utils.School
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.domain.model.UserModel
import com.maspam.etrain.training.presentation.profile.event.UpdateDataProfileFormEvent
import com.maspam.etrain.training.presentation.profile.viewmodel.UpdateDataProfileViewModel
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeDataProfilePage(
    scrollable: ScrollState = rememberScrollState(initial = 0),
    userModel: UserModel?,
    changeUpdateDataProfileViewModel: UpdateDataProfileViewModel,
    onBackPressed: () -> Unit,
    onUpdateSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {

    val state by changeUpdateDataProfileViewModel.state.collectAsStateWithLifecycle()

    eventListener(
        event = changeUpdateDataProfileViewModel.event
    ) { event ->
        when(event){
            is UpdateDataProfileViewModel.UserDataProfileEvent.Error -> {
                changeUpdateDataProfileViewModel.setError(event.e)
            }
            UpdateDataProfileViewModel.UserDataProfileEvent.Success -> {
               changeUpdateDataProfileViewModel.setSuccess(true)
            }
        }
    }

    if (state.isSuccess) {
        SuccessDialog(
            message = stringResource(R.string.update_profile_successfully)
        ) {
            changeUpdateDataProfileViewModel.setSuccess(false)
            onUpdateSuccess()
        }
    }

    val singlePickPicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { result ->
        result?.let {
            changeUpdateDataProfileViewModel.onChangeAction(
                action = UpdateDataProfileFormEvent.ProfileImageChange(it)
            )
        }
    }

    Scaffold(
        modifier = modifier
            .systemBarsPadding(),
        topBar = {
            TopBarWithArrowComponent(
                section = stringResource(R.string.edit_profile),
                onBackPress = onBackPressed
            )
        }
    ) { innerPadding ->

        val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
        var datePickerIsOpen by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .verticalScroll(state = scrollable),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 37.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.profileImage?.let { profile ->
                    AsyncImage(
                        model = profile,
                        contentDescription = "Image Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(shape = RoundedCornerShape(100))
                    )
                } ?: Image(
                    painterResource(R.drawable.profile_default),
                    contentDescription = "Image Default",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(100))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Change Profile",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
                    modifier = Modifier.clickable {
                        singlePickPicture.launch(
                            input = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                label = stringResource(R.string.name),
                valueInput = userModel?.name,
                hint = stringResource(R.string.input_name)
            ) {
                changeUpdateDataProfileViewModel.onChangeAction(action = UpdateDataProfileFormEvent.NameChange(it))
            }
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                readOnly = true,
                valueInput = userModel?.nip,
                label = stringResource(R.string.nip),
                hint = stringResource(R.string.input_nip)
            ) { }
            CustomEmailTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                valueInput = userModel?.email,
                label = stringResource(R.string.email)
            ) {
                changeUpdateDataProfileViewModel.onChangeAction(action = UpdateDataProfileFormEvent.EmailChange(it))
            }
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp),
                valueInput = userModel?.address,
                label = stringResource(R.string.address),
                hint = stringResource(R.string.input_address)
            ) {
                changeUpdateDataProfileViewModel.onChangeAction(action = UpdateDataProfileFormEvent.AddressChange(it))
            }
            CustomNumberTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                valueInput = userModel?.phoneNumber,
                label = stringResource(R.string.phone_number),
                hint = stringResource(R.string.input_phone_number)
            ) {
                changeUpdateDataProfileViewModel.onChangeAction(action = UpdateDataProfileFormEvent.AddressChange(it))
            }
            // Date Picker
            DatePickerField(
                label = stringResource(R.string.day_of_birth),
                value = convertMillisToDate(userModel?.dayOfBirth ?: 0),
                onCLick = { datePickerIsOpen = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
            )
            CustomFormChoice<Gender>(
                label = stringResource(R.string.gender),
                initialValue = userModel?.gender,
                onReceive = {
                    changeUpdateDataProfileViewModel.onChangeAction(action = UpdateDataProfileFormEvent.GenderChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            )
            CustomFormChoice<School>(
                label = stringResource(R.string.last_study),
                initialValue = userModel?.lastEducation,
                onReceive = {
                    changeUpdateDataProfileViewModel.onChangeAction(action = UpdateDataProfileFormEvent.LastStudyChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
            )
            CustomFormChoice<School>(
                label = stringResource(R.string.current_job_school),
                initialValue = userModel?.currentSchool,
                onReceive = {
                    changeUpdateDataProfileViewModel.onChangeAction(action = UpdateDataProfileFormEvent.CurrentSchoolChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomButtonFieldLoad(
                modifier = Modifier.padding(horizontal = 20.dp),
                isLoading = state.isLoading,
                buttonName = stringResource(R.string.update),
                buttonColor = MaterialTheme.colorScheme.primary
            ) {
                changeUpdateDataProfileViewModel.onChangeAction(action = UpdateDataProfileFormEvent.Submit(userModel = userModel ?: UserModel()))
            }
        }

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
                        changeUpdateDataProfileViewModel.onChangeAction(action = UpdateDataProfileFormEvent.DayOfBirthChange(
                            datePickerState.selectedDateMillis
                        ))
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

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

