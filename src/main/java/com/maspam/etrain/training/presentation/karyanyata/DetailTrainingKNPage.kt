package com.maspam.etrain.training.presentation.karyanyata

import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.composables.icons.lucide.LocateFixed
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.TimerReset
import com.maspam.etrain.R
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.convertMillisToDate
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.domain.model.TrainingModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.karyanyata.component.KaryaNyataItemComponent
import com.maspam.etrain.training.presentation.karyanyata.viewmodel.KaryaNyataViewModel

@Composable
fun DetailTrainingKNPage(
    karyaNyataViewModel: KaryaNyataViewModel,
    trainingModel: TrainingModel,
    scrollableState: ScrollState = rememberScrollState(initial = 0),
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    navigateToDetailKaryaNyata: (Int) -> Unit,
    navigateToLoginPage: () -> Unit,
) {

    LaunchedEffect(Unit) {
        karyaNyataViewModel.setInitialKaryaNyata(trainingModel.id ?: 0)
    }

    val state by karyaNyataViewModel.state.collectAsStateWithLifecycle()

    eventListener(
        karyaNyataViewModel.globalEvent
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                karyaNyataViewModel.setError(event.e)
            }
        }
    }

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {
                karyaNyataViewModel.setInitialKaryaNyata(trainingId = trainingModel.id ?: 0)
            }
        ) {
            karyaNyataViewModel.setError(e = null)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
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
                    model = constructUrl(trainingModel.image ?: ""),
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
                            text = trainingModel.typeTraining ?: stringResource(R.string.type_training),
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
                            text = convertMillisToDate(trainingModel.due ?: 0L),
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
                            text = trainingModel.typeTrainingCategory ?: stringResource(R.string.implementation),
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
                        text = if (trainingModel.location?.isBlank() == true) "-" else trainingModel.location ?: "-",
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
                        text = if (trainingModel.link?.isBlank() == true) "-" else trainingModel.link ?: "-",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                        overflow = TextOverflow.Clip,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                )
                Text(
                    text = trainingModel.name ?: stringResource(R.string.name_training),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    overflow = TextOverflow.Clip,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 15.dp)
                )
                Text(
                    text = trainingModel.desc ?: stringResource(R.string.description),
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
                        text = "Karya Nyata",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                state.enroll?.forEachIndexed { index, enroll ->
                    KaryaNyataItemComponent(
                        name = (index + 1).toString(),
                        status = enroll.karyaNyataModel?.status ?: stringResource(R.string.pending),
                        navigateToDetailKaryaNyata = { 
                            navigateToDetailKaryaNyata(enroll.karyaNyataModel?.id ?: 0)
                        },
                        modifier = Modifier.padding(bottom = 5.dp, start = 20.dp, end = 20.dp)
                    )
                }
            }

        }
    }
}