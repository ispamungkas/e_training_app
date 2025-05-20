package com.maspam.etrain.training.presentation.scanner

import android.media.MediaPlayer
import androidx.camera.core.CameraSelector
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.LoadingScreen
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.UnVerifyDialog
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import kotlinx.coroutines.delay
import java.util.concurrent.Executors

@Composable
fun ScannerPage(
    scannerViewModel: ScannerViewModel,
    navigateToLoginPage: () -> Unit,

    modifier: Modifier = Modifier
) {
    val state by scannerViewModel.state.collectAsState()

    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val executor = remember { Executors.newSingleThreadExecutor() }
    val previewView = remember {
        PreviewView(context)
    }
    val cameraController = remember { LifecycleCameraController(context) }
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

    // Setup Barcode
    val option = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()

    val barcodeScanner = BarcodeScanning.getClient(option)


    // Sound Scan
    val sCheck: MediaPlayer = MediaPlayer.create(context, R.raw.cek)

    // Setup ML KIT
    val analyzer = MlKitAnalyzer(
        listOf(barcodeScanner),
        COORDINATE_SYSTEM_VIEW_REFERENCED,
        ContextCompat.getMainExecutor(context),
    ) { result: MlKitAnalyzer.Result? ->
        showResult(result, barcodeScanner, state.isFirst ?: false, scannerViewModel, sCheck)
    }

    cameraController.apply {
        setImageAnalysisAnalyzer(
            executor,
            analyzer
        )
        bindToLifecycle(lifecycleOwner)
        cameraSelector = cameraxSelector
    }

    previewView.controller = cameraController

    val densitiy = LocalDensity.current

    state.isError?.let {
        it.ToComposable(
            isLoading = false,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {

            },
        ) {
            scannerViewModel.clear()
        }
    }

    state.isSuccess?.let {
        if (it && state.isVerified == true) {
            SuccessDialog(
                message = stringResource(R.string.certificate_verified)
            ) {
                scannerViewModel.clear()
            }
        }
    }

    state.isFailed?.let {
        UnVerifyDialog(
            message = stringResource(R.string.certificate_unverified)
        ) {
            scannerViewModel.clear()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) { innerPadding ->
        var visible by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
            LaunchedEffect(true) {
                delay(3000)
                visible = true
            }
            AnimatedVisibility(
                modifier = Modifier
                    .align(alignment = Alignment.Center),
                visible = visible,
                enter = slideInVertically {
                    with(densitiy) { -40.dp.roundToPx() }
                } + expandVertically(
                    expandFrom = Alignment.Top
                ) + fadeIn(
                    initialAlpha = 0.2f
                )
            ) {
                RectangleComponent(
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                )
            }

            if (state.isLoading == true) {
                LoadingScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun RectangleComponent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp))
        ) {
            Row(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Lucide.Info,
                    contentDescription = "Information",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.point_to_barcode),
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onPrimary)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(
                modifier = Modifier
                    .height(5.dp)
                    .width(80.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .height(5.dp)
                    .width(80.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .width(5.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .width(5.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        }
        Spacer(modifier = Modifier.height(250.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .width(5.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .width(5.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(
                modifier = Modifier
                    .height(5.dp)
                    .width(80.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .height(5.dp)
                    .width(80.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}

private fun showResult(
    result: MlKitAnalyzer.Result?,
    barcodeScanner: BarcodeScanner,
    isFirst: Boolean,
    scannerViewModel: ScannerViewModel,
    sound: MediaPlayer,
) {
    if (isFirst) {
        val barcodeResult = result?.getValue(barcodeScanner)
        if (barcodeResult != null && barcodeResult.size != 0 && barcodeResult.first() != null) {
            val res = barcodeResult[0].rawValue
            scannerViewModel.check(res ?: "")
            sound.start()
        }
    }

}

