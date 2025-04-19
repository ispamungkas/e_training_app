package com.maspam.etrain.training.presentation.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.presentation.dashboard.component.LoadingComponent
import com.maspam.etrain.training.presentation.dashboard.component.NewsItemComponent
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.news.viewmodel.ListNewsViewModel

@Composable
fun ListNewsPage(
    listNewsViewModel: ListNewsViewModel,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by listNewsViewModel.state.collectAsStateWithLifecycle()

    eventListener(
        listNewsViewModel.event
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                listNewsViewModel.setError(event.e)
            }
        }
    }

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = {},
            tryRequestAgain = {
                listNewsViewModel.apply {
                    setError(e = null)
                    getNews()
                }
            },
            onDismiss = { listNewsViewModel.setError(e = null) },
        )
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopBarWithArrowComponent(
                section = "Open Training",
                onBackPress = onBackPressed
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(top = 20.dp, bottom = 30.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(innerPadding)
        ) {
            if (state.isLoading) {
                items(4) {
                    LoadingComponent(size = 100.dp, modifier = Modifier.fillMaxWidth())
                }
            } else {
                state.listNews?.let {
                    items(items = it, key = { x -> x.publishDate ?: 0 }) { news ->
                        NewsItemComponent(
                            image = news.image,
                            newsHeadline = news.desc,
                            author = news.author,
                            postDate = news.publishDate,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 5.dp)
                        )
                    }

                } ?: item {
                    Text(
                        text = stringResource(R.string.no_news_for_today),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}