package com.maspam.etrain.training.presentation.report

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.maspam.etrain.training.core.presentation.component.FilterListComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.generateUserTrainingReport
import com.maspam.etrain.training.domain.model.UserModel
import com.maspam.etrain.training.presentation.dashboard.component.UserAccountReport
import java.time.LocalDate

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportPage(
    modifier: Modifier = Modifier,
    reportViewModel: ReportViewModel,
    navigateToLoginPage: () -> Unit,
    onUserClicked: (UserModel) -> Unit

) {
    val context = LocalContext.current
    val state by reportViewModel.state.collectAsState()
    val activity = LocalContext.current as Activity

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(context, "Storage Permission Granted!", Toast.LENGTH_SHORT).show()
            generatePdf(context, state.filteredData, state.year, activity)
        } else {
            Toast.makeText(context, "Storage Permission Denied.", Toast.LENGTH_SHORT).show()

        }
    }


    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                reportViewModel.apply {
                    setError(e = null)
                    getAllUser()
                }
            },
            onDismiss = { reportViewModel.setError(e = null) },
        )
    }

    Scaffold(
        modifier = modifier.systemBarsPadding(),
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefresh,
            onRefresh = {
                reportViewModel.refresh()
            }
        ) {
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                item {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp)
                    ) {
                        Text(text = "Select Year", style = MaterialTheme.typography.labelLarge)
                        Row(
                            modifier =  Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box {
                                OutlinedButton(
                                    onClick = {
                                        reportViewModel.expanded(true)
                                    }
                                ) {
                                    Text(state.year.toString(), style = MaterialTheme.typography.labelLarge)
                                }

                                DropdownMenu (
                                    expanded = state.expanded,
                                    onDismissRequest = { state.expanded = false }
                                ) {
                                    state.yearRange.forEach { year ->
                                        DropdownMenuItem(
                                            text = { Text(year.toString()) },
                                            onClick = {
                                                reportViewModel.setYear(year)
                                            }
                                        )
                                    }
                                }
                            }
                            OutlinedButton(
                                onClick = {
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    } else {
                                        generatePdf(context, state.filteredData, state.year, activity)
                                    }
                                }
                            ) {
                                Text("Download Report", style = MaterialTheme.typography.labelLarge)
                            }
                        }

                    }
                }

                item {
                    FilterListComponent(
                        filters = listOf("Inactive User", "Active User"),
                        onSelected = { filter ->
                            reportViewModel.getUserByCondition(filter)
                        }
                    )
                }

                items(items = state.filteredData ?: emptyList(), key = { it.id ?: 0 }) { data ->
                    UserAccountReport(
                        modifier = Modifier.clickable {
                            onUserClicked(data)
                        },
                        userModel = data,
                    )
                }
            }
        }
    }
}

@SuppressLint("NewApi")
private fun generatePdf(context: Context, users: List<UserModel>, tahun: Int, activity: Activity) {
    val current = LocalDate.now()
    val fileName = "Report ${current.year}.pdf"

    val contentResolver = context.contentResolver
    val contentValues = android.content.ContentValues().apply {
        put(android.provider.MediaStore.Downloads.DISPLAY_NAME, fileName)
        put(android.provider.MediaStore.Downloads.MIME_TYPE, "application/pdf")
        put(android.provider.MediaStore.Downloads.IS_PENDING, 1)
    }

    val uri = contentResolver.insert(android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        contentResolver.openOutputStream(uri)?.use { outputStream ->
            val success = generateUserTrainingReport(users, outputStream, tahun)
            contentValues.clear()
            contentValues.put(android.provider.MediaStore.Downloads.IS_PENDING, 0)
            contentResolver.update(uri, contentValues, null, null)

            activity.runOnUiThread {
                if (success) {
                    Toast.makeText(context, "PDF saved to Downloads!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Failed to generate PDF.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    } ?: run {
        Toast.makeText(context, "Unable to access Downloads folder.", Toast.LENGTH_SHORT).show()
    }
}
