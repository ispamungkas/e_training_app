package com.maspam.etrain.training.presentation.news

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.maspam.etrain.R
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.presentation.component.ConfirmationBottomSheetFlexComponent
import com.maspam.etrain.training.core.presentation.component.CustomButtonField
import com.maspam.etrain.training.core.presentation.component.CustomTextField
import com.maspam.etrain.training.core.presentation.component.LoadingScreen
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.core.presentation.utils.getFileSizeInMB
import com.maspam.etrain.training.data.dto.body.NewsBody
import com.maspam.etrain.training.domain.model.NewsModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.news.event.FormNewsEvent
import com.maspam.etrain.training.presentation.news.viewmodel.NewsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormEditNewsPage(
    modifier: Modifier = Modifier,
    newsViewModel: NewsViewModel,
    newsModel: NewsModel,
    navigateToLoginPage: () -> Unit,
    onBackPressed: () -> Unit
) {

    LaunchedEffect(newsModel) {
        newsViewModel.setInitialValue(newsModel)
    }

    val state by newsViewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val filename: String? = state.img.path?.substring((state.img.path?.lastIndexOf("/")?.plus(1)!!))

    val singlePickPicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { result ->
        result?.let {
            val checkSize = getFileSizeInMB(context = context, uri = it)
            if (checkSize > 2.0) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.image_size_more_than_2mb),
                        actionLabel = context.getString(R.string.close),
                        duration = SnackbarDuration.Short
                    )
                }
            } else {
                newsViewModel.onChangeAction(
                    action = FormNewsEvent.ImgChange(it)
                )
            }
        }
    }

    eventListener(
        newsViewModel.globalEvent
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                newsViewModel.setError(event.e)
            }
        }
    }

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                newsViewModel.apply {
                    setError(e = null)
                }
            }
        ) {
            newsViewModel.setError(e = null)
        }
    }

    if (state.isSuccess) {
        SuccessDialog(
            message = "News successfully updated"
        ) {
            onBackPressed()
        }
    }

    Scaffold(
        modifier = modifier
            .systemBarsPadding()
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopBarWithArrowComponent(
                section = stringResource(R.string.edit_news)
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
                        if (filename?.isNotBlank() == true) {
                            AsyncImage(
                                model = state.img,
                                contentScale = ContentScale.Crop,
                                error = painterResource(R.drawable.news_image_sample),
                                contentDescription = "News Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(horizontal = 20.dp, vertical = 10.dp)
                                    .clip(shape = RoundedCornerShape(15.dp))
                            )
                        } else {
                            AsyncImage(
                                model = constructUrl(newsModel.image ?: ""),
                                contentScale = ContentScale.Crop,
                                error = painterResource(R.drawable.news_image_sample),
                                contentDescription = "News Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(horizontal = 20.dp, vertical = 10.dp)
                                    .clip(shape = RoundedCornerShape(15.dp))
                            )
                        }
                        CustomTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp, start = 20.dp, end = 20.dp),
                            valueInput = newsModel.name ?: state.name,
                            readOnly = false,
                            label = stringResource(R.string.name_training),
                            errorMessage = state.nameError,
                            hint = stringResource(R.string.name_training)
                        ) {
                            newsViewModel.onChangeAction(action = FormNewsEvent.NameChange(it))
                        }
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

                        if (state.imgError.isNotBlank()) {
                            Text(
                                text = state.imgError,
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
                            valueInput = newsModel.desc ?: state.desc,
                            singleLine = false,
                            readOnly = false,
                            label = stringResource(R.string.description),
                            errorMessage = state.descError,
                            hint = stringResource(R.string.input_description_of_training)
                        ) {
                            newsViewModel.onChangeAction(action = FormNewsEvent.DescChange(it))
                        }
                        Spacer(modifier = Modifier.height(110.dp))
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
                newsViewModel.showConfirmationModal(value = true)
            }
            if (state.userConfirmation) {
                ModalBottomSheet(
                    onDismissRequest = {
                        newsViewModel.showConfirmationModal(value = false)
                    },
                    sheetState = sheetState,
                ) {
                    ConfirmationBottomSheetFlexComponent(
                        message = stringResource(R.string.are_you_sure_the_data_is_valid),
                        isLoading = state.isLoading ?: false,
                        onDismiss = {
                            newsViewModel.showConfirmationModal(false)
                        }
                    ) {
                        newsViewModel.showConfirmationModal(false)
                        newsViewModel.onChangeAction(
                            action = FormNewsEvent.Update(
                                newsBody = NewsBody(
                                    id = newsModel.id,
                                    name = state.name,
                                    desc = state.desc,
                                    image = state.img,
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