package com.maspam.etrain.training.core.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.maspam.etrain.R
import kotlinx.coroutines.delay

@Composable
fun NoInternetDialog(
    message: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onDismissDialog: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissDialog,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
        Card(
            modifier = modifier,
        ) {
            Column(
                modifier = Modifier.padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.no_internet_display),
                    contentDescription = message,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.no_internet_access),
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                )

                CustomButtonFieldLoad(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    buttonName = stringResource(R.string.retry),
                    isLoading = isLoading,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun RequestTimeOut(modifier: Modifier = Modifier) {
    var show by remember { mutableStateOf(true) }
    if (show) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                usePlatformDefaultWidth = true
            )
        ) {
            Card(
                modifier = modifier,
            ) {
                Column(
                    modifier = Modifier.padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.request_timeout_display),
                        contentDescription = stringResource(R.string.request_timeout),
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.timeout_word),
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                    )
                }
            }
        }
    }
}

@Composable
fun UnAuthorizedErrorDialog(
    message: String,
    modifier: Modifier = Modifier,
    onResponse: () -> Unit
) {
    var show by remember { mutableStateOf(true) }
    if (show) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )
        ) {

            LaunchedEffect(true) {
                delay(2000L)
                onResponse()
            }

            Card(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 30.dp
                        ),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = message,
                        overflow = TextOverflow.Clip,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                    )
                }
            }
        }
    }
}

@Composable
fun BadRequestDialog(modifier: Modifier = Modifier, onDismissDialog: () -> Unit) {
    var show by remember { mutableStateOf(true) }
    if (show) {
        Dialog(
            onDismissRequest = onDismissDialog,
            properties = DialogProperties(
                usePlatformDefaultWidth = true
            )
        ) {
            Card(
                modifier = modifier,
            ) {
                Column(
                    modifier = Modifier.padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.danger_display),
                        contentDescription = stringResource(R.string.bad_request),
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.bad_request_word),
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
                    )
                }
            }
        }
    }
}

@Composable
fun NotAcceptedDialog(modifier: Modifier = Modifier, onDismissDialog: () -> Unit) {
    var show by remember { mutableStateOf(true) }
    if (show) {
        Dialog(
            onDismissRequest = onDismissDialog,
        ) {
            Card(
                modifier = modifier,
            ) {
                Column(
                    modifier = Modifier.padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.danger_display),
                        contentDescription = stringResource(R.string.not_accepted),
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.not_accepted_word),
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
                    )
                }
            }
        }
    }
}

@Composable
fun NotFoundDialog(modifier: Modifier = Modifier) {
    var show by remember { mutableStateOf(true) }
    if (show) {
        Dialog(
            onDismissRequest = {
                show = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = true
            )
        ) {
            Card(
                modifier = modifier,
            ) {
                Column(
                    modifier = Modifier.padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.not_found_display),
                        contentDescription = stringResource(R.string.not_found),
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.not_found_word),
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
                    )
                }
            }
        }
    }
}


@Composable
fun SuccessDialog(message: String, modifier: Modifier = Modifier, onDismissDialog: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success2))
    var play by remember { mutableStateOf(true) }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = play
    )

    LaunchedEffect(key1 = progress) {
        if (progress == 1f) {
            play = false
        }
    }

    Dialog(
        onDismissRequest = onDismissDialog,
        properties = DialogProperties(
            usePlatformDefaultWidth = true
        )
    ) {
        Card(
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 30.dp,
                        vertical = 30.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LottieAnimation(composition = composition, modifier = Modifier.size(150.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun Componentz(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 30.dp,
                    vertical = 30.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Sddsfsfsfsfsdfdsfdsfsfdsfsfsfs",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview
@Composable
private fun NoInternetDialogPrev() {
    Componentz()
}