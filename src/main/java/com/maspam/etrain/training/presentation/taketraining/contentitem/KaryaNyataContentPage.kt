package com.maspam.etrain.training.presentation.taketraining.contentitem

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.TopBarWithStartImage
import com.maspam.etrain.training.core.presentation.utils.getFileSizeInMB
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun KaryaNyataContentPage(
    modifier: Modifier = Modifier,
    karyaNyataStatus: String?,
    file: Uri? = null,
    grade: String? = null,
    isSuccess: Boolean,
    onDismissDialog: () -> Unit,
    onPickedFile: (Uri) -> Unit,
    submitKaryaNyata: () -> Unit,
    onMenuClicked: () -> Unit
) {

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val singlePickFile = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { result ->
        result?.let {
            val checkSize = getFileSizeInMB(context = context, uri = it)
            if (checkSize > 5.0) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.file_size_more_than_5mb),
                        actionLabel = context.getString(R.string.close),
                        duration = SnackbarDuration.Short
                    )
                }
            } else {
                onPickedFile(it)
            }
        }
    }

    val filename: String? = file?.path?.substring((file.path?.lastIndexOf("/")?.plus(1)!!))

    if (isSuccess) {
        SuccessDialog(
            message = stringResource(R.string.karya_nyata_successfully_uploaded),
            onDismissDialog = onDismissDialog
        )
    }

    Scaffold(
        modifier = modifier
            .systemBarsPadding(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopBarWithStartImage(
                name = "Karya Nyata"
            ) {
                onMenuClicked()
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                Text(
                    text = "Please input your assignment for “Karya Nyata” that contain on this training material !!!",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    grade?.let {
                        Text(
                            text = stringResource(R.string.grade, it),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    } ?: Text(
                        text = stringResource(R.string.grade, "-"),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .background(
                                color = when (karyaNyataStatus ?: "") {
                                    "pending" -> Color.Yellow.copy(0.1f)
                                    "decline" -> Color.Red.copy(0.1f)
                                    "accepted" -> Color.Green.copy(0.1f)
                                    else -> Color.Yellow.copy(0.1f)
                                },
                                shape = RoundedCornerShape(15.dp)
                            )
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 15.dp, 10.dp)
                                .align(alignment = Alignment.Center),
                            text = if (karyaNyataStatus?.isEmpty() == true) "Not Submitted" else karyaNyataStatus
                                ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = when (karyaNyataStatus) {
                                    "pending" -> Color.Yellow
                                    "decline" -> Color.Red
                                    "accepted" -> Color.Green
                                    else -> Color.Yellow
                                },
                            )
                        )
                    }

                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                        .height(100.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clickable {
                            if (karyaNyataStatus == "decline" || karyaNyataStatus == "") {
                                singlePickFile.launch(
                                    input = arrayOf("application/pdf")
                                )
                            }
                        }
                ) {
                    Text(
                        text = if (karyaNyataStatus == "pending" || karyaNyataStatus == "accepted") {
                            "File uploaded"
                        } else {
                            filename ?: "Choose your file"
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onPrimary),
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                    )
                }
                Text(
                    text = "* The file must be .pdf format",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.error
                    )
                )
                FilledIconButton(
                    enabled = if (karyaNyataStatus == "decline" || karyaNyataStatus == "") true else false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 20.dp),
                    onClick = {
                        if (file == null) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "File ${context.getString(R.string.empty)}",
                                    actionLabel = context.getString(R.string.close),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        } else {
                            submitKaryaNyata()
                        }
                    }
                ) {
                    Text(
                        text = "Submit",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onPrimary)
                    )
                }
            }

        }
    }
}