package com.maspam.etrain.training.presentation.training

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.CustomButtonField
import com.maspam.etrain.training.core.presentation.component.CustomTextField
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.domain.model.PostTestModel
import com.maspam.etrain.training.domain.model.SectionModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.viewmodel.PostTestViewModel

@Composable
fun FormAddPostTest(
    modifier: Modifier = Modifier,
    postTestViewModel: PostTestViewModel,
    postTestModel: PostTestModel? = null,
    section: SectionModel,
    trainingId: Int,
    onBackPressed: () -> Unit,
) {

    val context = LocalContext.current
    val state by postTestViewModel.state.collectAsState()

    if (postTestModel != null) {
        postTestViewModel.setAllReadyQuestion(postTestModel.question)
    }

    eventListener(
        postTestViewModel.globalEvent
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                postTestViewModel.setError(event.e)
            }
        }
    }

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = {},
            tryRequestAgain = {
                if (postTestModel == null) {
                    postTestViewModel.apply {
                        setError(e = null)
                        createPostTest(
                            sectionId = section.id ?: 0,
                            question = listOf(state.question1, state.question2, state.question3),
                            trainId = trainingId
                        )
                    }
                } else {
                    postTestViewModel.apply {
                        setError(e = null)
                        postTestModel.id?.let { it1 ->
                            updatePostTest(
                                question = listOf(state.question1, state.question2, state.question3),
                                postTestId = it1
                            )
                        }
                    }
                }
            }
        ) {
            postTestViewModel.setError(e = null)
        }
    }

    if (state.isSuccessful) {
        SuccessDialog(
            message = stringResource(R.string.post_test_successfully_updated)
        ) {
            onBackPressed()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            TopBarWithArrowComponent(
                section = stringResource(R.string.post_test_content)
            ) {
                onBackPressed()
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.this_page_is_creating_a_question_each_topic),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                            modifier = Modifier.padding(vertical = 15.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            Text(
                                text = section.name ?: stringResource(R.string.section_name),
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                                modifier = Modifier.padding(15.dp)
                            )
                        }
                        HorizontalDivider(
                            thickness = 2.dp,
                            modifier = Modifier.padding(vertical = 25.dp)
                        )
                        CustomTextField(
                            modifier = Modifier
                                .padding(bottom = 10.dp),
                            valueInput = postTestModel?.question?.get(0),
                            label = stringResource(R.string.question, 1),
                            singleLine = false,
                            hint = stringResource(R.string.input_question)
                        ) { value ->
                            postTestViewModel.setQuestion(1, value)
                        }
                        CustomTextField(
                            modifier = Modifier
                                .padding(bottom = 10.dp),
                            valueInput = postTestModel?.question?.get(1),
                            label = stringResource(R.string.question, 2),
                            singleLine = false,
                            hint = stringResource(R.string.input_question)
                        ) { value ->
                            postTestViewModel.setQuestion(2, value)
                        }
                        CustomTextField(
                            modifier = Modifier
                                .padding(bottom = 10.dp),
                            valueInput = postTestModel?.question?.get(2),
                            label = stringResource(R.string.question, 3),
                            singleLine = false,
                            hint = stringResource(R.string.input_question)
                        ) { value ->
                            postTestViewModel.setQuestion(3, value)
                        }
                    }
                }
            }
            CustomButtonField(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.BottomCenter)
                    .padding(vertical = 20.dp, horizontal = 20.dp),
                buttonName = stringResource(R.string.submit),
                buttonColor = MaterialTheme.colorScheme.primary,
            ) {
                if (
                    state.question1.isEmpty() || state.question2.isEmpty() || state.question3.isEmpty()
                ) {
                    Toast.makeText(context, "Please fill all question", Toast.LENGTH_SHORT).show()
                    return@CustomButtonField
                }
               if (postTestModel == null) {
                   postTestViewModel.createPostTest(
                       sectionId = section.id ?: 0,
                       question = listOf(state.question1, state.question2, state.question3),
                       trainId = trainingId
                   )
               } else {
                   postTestViewModel.updatePostTest(
                       question = listOf(state.question1, state.question2, state.question3),
                       postTestId = postTestModel.id ?: 0
                   )
               }
            }
        }
    }
}