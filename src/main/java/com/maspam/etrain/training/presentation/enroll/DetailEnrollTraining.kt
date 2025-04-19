package com.maspam.etrain.training.presentation.enroll

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Play
import com.maspam.etrain.R
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.domain.model.EnrollModel

@Composable
fun DetailEnrollTraining(
    enroll: EnrollModel,
    scrollableState: ScrollState = rememberScrollState(initial = 0),
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopBarWithArrowComponent(
                section = "Detail Training",
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
                )
                Text(
                    text = enroll.trainingDetail?.name ?: stringResource(R.string.name_training),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    overflow = TextOverflow.Clip,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 15.dp)
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
            Box(
                modifier = Modifier
                    .padding(end = 20.dp, bottom = 20.dp)
                    .clip(shape = RoundedCornerShape(30.dp))
                    .background(color = MaterialTheme.colorScheme.primary)
                    .align(alignment = Alignment.BottomEnd)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .padding(horizontal = 30.dp, vertical = 10.dp)
                ) {
                    Text(
                        "Continue",
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