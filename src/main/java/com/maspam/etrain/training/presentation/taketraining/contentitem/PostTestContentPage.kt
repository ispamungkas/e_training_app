package com.maspam.etrain.training.presentation.taketraining.contentitem

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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.CustomButtonFieldLoad
import com.maspam.etrain.training.core.presentation.component.InputFailure
import com.maspam.etrain.training.core.presentation.component.LoadingScreen
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.TopBarWithStartImage
import com.maspam.etrain.training.domain.model.PostTestModel
import com.maspam.etrain.training.domain.model.SectionModel

@Composable
fun PostTestContentPage(
    modifier: Modifier = Modifier,
    postTest: List<PostTestModel>,
    section: List<SectionModel>,
    listAnswer: List<List<String>?>?,
    isLoading: Boolean,
    isSuccess: Boolean,
    inputFailure: Boolean,
    onDismissDialog: () -> Unit,
    onDismissFailureInputDialog: () -> Unit,
    changeAnswerValue: (Int, Int, String) -> Unit,
    onSubmit: () -> Unit,
    onMenuCLicked: () -> Unit
) {

    if (isSuccess) {
        SuccessDialog(
            message = stringResource(R.string.post_test_upload_success),
            onDismissDialog = onDismissDialog
        )
    }

    if (inputFailure) {
        InputFailure(
            message = stringResource(R.string.input_in_order)
        ) {
            onDismissFailureInputDialog()
        }
    }

    Scaffold(
        modifier = modifier
            .systemBarsPadding(),
        topBar = {
            TopBarWithStartImage(
                name = stringResource(R.string.post_test)
            ) {
                onMenuCLicked()
            }
        }
    ) { innerPadding ->

        if (isLoading) {
            LoadingScreen()
        }

        Box(
            modifier = modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                item {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = 20.dp),
                                text = stringResource(R.string.please_choose_the_answer_that_best_suits_your_personality),
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                            )
                            postTest.forEachIndexed { index, postTestModel ->
                                Column(
                                    modifier = Modifier.padding(top = 15.dp)
                                ) {
                                    Text(
                                        text = section.first { it.id == postTestModel.section }.name
                                            ?: "",
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                                    )
                                    Divider(
                                        thickness = 2.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 15.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.post_test_opening),
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
                                    )
                                }
                                postTestModel.question?.forEachIndexed { index2, question ->
                                    Text(
                                        modifier = Modifier.padding(vertical = 15.dp),
                                        text = "${index2 + 1}. $question ",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 10.dp),
                                        text = stringResource(R.string.answer),
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
                                    )
                                    TextField(
                                        modifier = Modifier
                                            .padding(vertical = 5.dp)
                                            .fillMaxWidth()
                                            .clip(shape = RoundedCornerShape(15.dp)),
                                        value = try {
                                            listAnswer?.get(index)?.get(index2) ?: ""
                                        } catch (e: IndexOutOfBoundsException) {
                                            ""
                                        },
                                        onValueChange = { value ->
                                            changeAnswerValue(index, index2, value)
                                        },
                                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Normal,
                                            lineHeight = 20.sp
                                        ),
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(70.dp))
                        }
                    }
                }
            }
            CustomButtonFieldLoad(
                buttonName = stringResource(R.string.submit),
                buttonColor = MaterialTheme.colorScheme.primary,
                isLoading = false,
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                onSubmit()
            }
        }

    }
}
