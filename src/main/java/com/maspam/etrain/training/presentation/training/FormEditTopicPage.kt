package com.maspam.etrain.training.presentation.training

import android.widget.Toast
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.ConfirmationBottomSheetFlexComponent
import com.maspam.etrain.training.core.presentation.component.CustomButtonField
import com.maspam.etrain.training.core.presentation.component.CustomTextField
import com.maspam.etrain.training.core.presentation.component.LoadingScreen
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.EditorControls
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.data.dto.body.TopicBody
import com.maspam.etrain.training.domain.model.TopicModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.event.FormTopicTrainingEvent
import com.maspam.etrain.training.presentation.training.viewmodel.ListTopicViewModel
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormEditTopicPage(
    listTopicViewModel: ListTopicViewModel,
    topicModel: TopicModel,
    modifier: Modifier = Modifier,
    navigateToLoginPage: () -> Unit,
    onBackPressed: () -> Unit
) {

    val richState = rememberRichTextState()

    LaunchedEffect(Unit) {
        listTopicViewModel.selectedTopicViewModel(value = topicModel)
        richState.setHtml(topicModel.content ?: "")
    }

    val state by listTopicViewModel.state.collectAsStateWithLifecycle()
    val selectedTopic = state.selectedData
    val sheetState = rememberModalBottomSheetState()
    val titleSize = MaterialTheme.typography.displaySmall.fontSize
    val subtitleSize = MaterialTheme.typography.titleLarge.fontSize
    val context = LocalContext.current

    val filename: String? = state.selectedData?.img?.path?.substring((state.selectedData?.img?.path?.lastIndexOf("/")?.plus(1)!!))

    val singlePickPicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { result ->
        result?.let {
            listTopicViewModel.onChangeAction(
                action = FormTopicTrainingEvent.ImgChange(it)
            )
        }
    }

    eventListener(
        listTopicViewModel.globalEvent
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                listTopicViewModel.setError(event.e)
            }
        }
    }

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                listTopicViewModel.apply {
                    setError(e = null)
                    updateTopic(state.selectedData?.copy(content = richState.toHtml()) ?: TopicBody())
                }
            }
        ) {
            listTopicViewModel.setError(e = null)
        }
    }

    if (state.isSuccess) {
        SuccessDialog(
            message = "Topic successfully updated"
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
                section = stringResource(R.string.topic_section)
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
                            valueInput = topicModel.name,
                            readOnly = false,
                            label = stringResource(R.string.name_training),
                            hint = stringResource(R.string.name_training)
                        ) {
                            listTopicViewModel.onChangeAction(action = FormTopicTrainingEvent.NameChange(it))
                        }
                        HorizontalDivider(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 20.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                                .border(
                                    width = 2.dp,
                                    color = if (state.isPictureWasAdded == true) Color.Green else MaterialTheme.colorScheme.primary,
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

                        EditorControls(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            state = richState,
                            onBoldClick = {
                                richState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            },
                            onItalicClick = {
                                richState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
                            },
                            onUnderlineClick = {
                                richState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                            },
                            onTitleClick = {
                                richState.toggleSpanStyle(SpanStyle(fontSize = titleSize))
                            },
                            onSubtitleClick = {
                                richState.toggleSpanStyle(SpanStyle(fontSize = subtitleSize))
                            },
                            onTextColorClick = {
                                richState.toggleSpanStyle(SpanStyle(color = Color.Red))
                            },
                            onStartAlignClick = {
                                richState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Start))
                            },
                            onEndAlignClick = {
                                richState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.End))
                            },
                            onCenterAlignClick = {
                                richState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center))
                            },
                            onExportClick = {
                                FormTopicTrainingEvent.ContentChange(richState.toHtml())
                                Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show()
                            },
                            valueSave = {
                                FormTopicTrainingEvent.ContentChange(richState.toHtml())
                                Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show()
                            }
                        )
                        RichTextEditor(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .padding(horizontal = 20.dp),
                            state = richState,
                        )

                        if (state.contentError?.isNotBlank() == true) {
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = state.contentError ?: "",
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                            )
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
                listTopicViewModel.setShowConfirmation(value = true)
            }
            if (state.showConfirmation) {
                ModalBottomSheet(
                    onDismissRequest = {
                        listTopicViewModel.setShowConfirmation(value = false)
                    },
                    sheetState = sheetState,
                ) {
                    ConfirmationBottomSheetFlexComponent(
                        message = stringResource(R.string.are_you_sure_the_data_is_valid),
                        isLoading = state.isLoading,
                        onDismiss = {
                            listTopicViewModel.setShowConfirmation(value = false)
                        }
                    ) {

                        listTopicViewModel.setShowConfirmation(value = false)
                        listTopicViewModel.onChangeAction(
                            action = FormTopicTrainingEvent.Submit(
                                topicBody = selectedTopic?.copy(content = richState.toHtml()) ?: TopicBody()
                            )
                        )

                    }
                }
            }
            if (state.isLoading) {
                LoadingScreen()
            }
        }
    }
}