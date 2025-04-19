package com.maspam.etrain.training.presentation.dashboard.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.maspam.etrain.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageSliderComponent(
    data: List<Painter> = listOf(
        painterResource(R.drawable.banner_sample),
        painterResource(R.drawable.banner_sample),
        painterResource(R.drawable.banner_sample),
    ),
    modifier: Modifier = Modifier
) {
    val sliderState = rememberPagerState(initialPage = 0)

    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(2600)
            sliderState.animateScrollToPage(
                page = (sliderState.currentPage + 1) % (sliderState.pageCount)
            )
        }
    }

    HorizontalPager(
        count = data.size,
        state = sliderState,
        contentPadding = PaddingValues(horizontal = 20.dp),
        modifier = modifier
            .height(150.dp)
            .fillMaxWidth()
    ) { page ->
        Box {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                Image(
                    painter = data[page],
                    contentDescription = "slider",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            HorizontalPagerIndicator(
                pagerState = sliderState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            )
        }
    }
}

@Preview
@Composable
private fun cek() {
    ImageSliderComponent()
}