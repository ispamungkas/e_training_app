package com.maspam.etrain.training.presentation.training

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Trash
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.ShimmerEffect
import com.maspam.etrain.training.core.presentation.component.SuccessDialog
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.domain.model.SectionModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.viewmodel.ListSectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTopicPage(
    listSectionViewModel: ListSectionViewModel,
    section: List<SectionModel>?,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {

    LaunchedEffect(true) {
        listSectionViewModel.setInitialValue(section ?: emptyList())
    }

    val state by listSectionViewModel.state.collectAsStateWithLifecycle()

    eventListener(
        listSectionViewModel.globalEvent
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                listSectionViewModel.setError(event.e)
            }
        }
    }

    if (state.isSuccess) {
        SuccessDialog(
            message = stringResource(R.string.section_berhasil_terupdate)
        ) {
            listSectionViewModel.dismissAlert()
        }
    }

    Scaffold(
        modifier = modifier
            .systemBarsPadding()
            .fillMaxSize(),
        topBar = {
            TopBarWithArrowComponent(
                modifier = Modifier
                    .padding(vertical = 20.dp),
                section = stringResource(R.string.material_section),
                onBackPress = onBackPressed
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(20.dp),
                    imageVector = Lucide.Plus,
                    contentDescription = "Icon Add"
                )
            }
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefresh,
            onRefresh = {
                listSectionViewModel.run {
                    refresh()
                    getAllSection()
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {

                if (state.isLoading) {
                    items(count = 5) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                        ) {
                            ShimmerEffect(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                items(items = state.data ?: emptyList(), key = { it.id ?: 0 }) { data ->
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(15.dp)),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = data.name ?: stringResource(R.string.section_name),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal),
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Icon(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                        listSectionViewModel.deleteSection(data.id ?: 0)
                                    },
                                imageVector = Lucide.Trash,
                                contentDescription = "Icon Trash"
                            )
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}