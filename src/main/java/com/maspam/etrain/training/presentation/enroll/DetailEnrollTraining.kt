package com.maspam.etrain.training.presentation.enroll

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.LocateFixed
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Play
import com.composables.icons.lucide.TimerReset
import com.maspam.etrain.R
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.core.presentation.utils.toDateTimeFormatter
import com.maspam.etrain.training.domain.model.EnrollModel
import com.maspam.etrain.training.presentation.enroll.viewmodel.EnrollViewModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent

@Composable
fun DetailEnrollTraining(
    enrollViewModel: EnrollViewModel,
    enroll: EnrollModel,
    scrollableState: ScrollState = rememberScrollState(initial = 0),
    onBackPressed: () -> Unit,
    navigateToTakeTraining: () -> Unit,
    modifier: Modifier = Modifier
) {

    val state by enrollViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    eventListener(
        enrollViewModel.globalEvent
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                enrollViewModel.setError(event.e)
            }
        }
    }

    if (state.isSuccess) {
        SuccessDialog(
            message = "Successful attendance"
        ) {
            enrollViewModel.dismissAlert()
        }
    }

    if (state.downloadSuccess) {
        SuccessDialog(
            message = "Certificate created"
        ) {
            enrollViewModel.dismissAlert()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopBarWithArrowComponent(
                section = stringResource(R.string.detail_training),
                onBackPress = onBackPressed
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollableState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.TopCenter)
            ) {
                AsyncImage(
                    model = constructUrl(enroll.trainingDetail?.image ?: ""),
                    contentDescription = "Image Training",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(start = 20.dp, top = 20.dp, end = 20.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(R.drawable.icon_category),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "Icon Category"
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = enroll.trainingDetail?.typeTraining ?: stringResource(R.string.type_training),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material.Icon(
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary,
                            imageVector = Lucide.TimerReset,
                            contentDescription = "Icon Category"
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = enroll.trainingDetail?.due?.toDateTimeFormatter() ?: stringResource(R.string.due_date),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary,
                            imageVector = Lucide.LocateFixed,
                            contentDescription = "Icon Location"
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = enroll.trainingDetail?.typeTrainingCategory ?: stringResource(R.string.implementation),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                )
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.location),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        overflow = TextOverflow.Clip,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (enroll.trainingDetail?.location?.isBlank() == true) "-" else enroll.trainingDetail?.location ?: "-",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                        overflow = TextOverflow.Clip,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.link),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        overflow = TextOverflow.Clip,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (enroll.trainingDetail?.link?.isBlank() == true) "-" else enroll.trainingDetail?.link ?: "-",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                        overflow = TextOverflow.Clip,
                        modifier = Modifier
                            .clickable {
                                if (enroll.trainingDetail?.link?.isBlank() != true) {
                                    uriHandler.openUri(enroll.trainingDetail?.link ?: "https://google.com")
                                }
                            }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.attendance),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        overflow = TextOverflow.Clip,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (enroll.attandance == true || state.data?.attandance == true) "Yes" else "no",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                        overflow = TextOverflow.Clip,
                    )
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                )
                Text(
                    text = enroll.trainingDetail?.name ?: stringResource(R.string.name_training),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    overflow = TextOverflow.Clip,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 15.dp)
                )
                Text(
                    text = enroll.trainingDetail?.desc ?: stringResource(R.string.description),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
                    overflow = TextOverflow.Clip,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 20.dp
                        )
                ) {
                    Text(
                        text = "${enroll.trainingDetail?.sections?.size} Session",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                enroll.trainingDetail?.sections?.forEach { section ->
                    SectionItemComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 50.dp),
                        name = section.name,
                        totalJp = section.jp
                    )
                }
            }

            if (
                enroll.trainingDetail?.typeTraining == "webinar" && enroll.attandance == true ||
                state.data?.trainingDetail?.typeTraining == "webinar" && state.data?.attandance == true
                ) {
                Box(
                    modifier = Modifier
                        .padding(end = 20.dp, bottom = 80.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(color = MaterialTheme.colorScheme.primary)
                        .align(alignment = Alignment.BottomEnd)
                        .clickable {
                            enrollViewModel.getCertificate(
                                enrollId = enroll.id ?: 0
                            )
                            if (state.certificate?.cert?.isNotBlank() == true || enroll.certificate?.cert?.isNotBlank() == true) {
                                val url = state.certificate?.cert?.removeRange(0..5)
                                val intent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(constructUrl(url ?: "")))
                                context.startActivity(intent, null)
                            }
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .padding(horizontal = 30.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Download Certificate",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                        Icon(
                            imageVector = Lucide.ChevronRight,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Icon Next",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .padding(end = 20.dp, bottom = 20.dp)
                    .clip(shape = RoundedCornerShape(30.dp))
                    .background(color = MaterialTheme.colorScheme.primary)
                    .align(alignment = Alignment.BottomEnd)
                    .clickable {
                        if (enroll.trainingDetail?.typeTraining == "webinar") {
                            enrollViewModel.setAbsence(enrollId = enroll.id ?: 0)
                        } else {
                            navigateToTakeTraining()
                        }
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .padding(horizontal = 30.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = if (enroll.trainingDetail?.typeTraining == "webinar") "Absenteeism" else stringResource(R.string.get_started),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    Icon(
                        imageVector = Lucide.ChevronRight,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = "Icon Next",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

        }
    }
}

@Composable
fun SectionItemComponent(
    name: String? = "",
    totalJp: Int? = 0,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Lucide.Play,
            contentDescription = "Play Icon",
            modifier = Modifier
                .size(24.dp)
                .weight(0.10f),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier
                .weight(0.60f)
                .padding(horizontal = 10.dp),
            text = name ?: stringResource(R.string.section_name),
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.weight(0.10f),
            text = "$totalJp Jp",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            overflow = TextOverflow.Ellipsis
        )
    }
}