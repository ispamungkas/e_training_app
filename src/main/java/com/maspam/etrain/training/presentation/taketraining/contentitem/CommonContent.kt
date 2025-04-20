package com.maspam.etrain.training.presentation.taketraining.contentitem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Menu
import com.composables.icons.lucide.Timer
import com.maspam.etrain.R
import com.maspam.etrain.training.core.presentation.component.HtmlText

@Composable
fun CommonContentPage(modifier: Modifier = Modifier, onMenuClicked : () -> Unit) {
    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding)
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Image(
                        alignment = Alignment.TopStart,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(235.dp),
                        painter = painterResource(R.drawable.news_image_sample),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Image Content",
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = 25.dp, start = 20.dp)
                            .size(50.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(100)
                            )
                            .align(alignment = Alignment.TopStart)
                            .clickable {
                                onMenuClicked()
                            }
                    ) {
                        Icon(
                            modifier = Modifier
                                .align(alignment = Alignment.Center),
                            imageVector = Lucide.Menu,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Icon menu"
                        )
                    }
                    Card(
                        colors = CardDefaults.cardColors()
                            .copy(containerColor = MaterialTheme.colorScheme.background),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .align(Alignment.TopCenter)
                            .padding(top = 210.dp),
                        shape = RoundedCornerShape(
                            topStart = 15.dp,
                            topEnd = 15.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(
                                    horizontal = 20.dp,
                                    vertical = 25.dp
                                )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Literacy",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Lucide.Timer,
                                        contentDescription = "Icon Timer",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        "45 Menit",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium)
                                    )
                                }
                            }
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 15.dp)
                            )
                            val htmlContent = """
<h1>How to create <a href="https://medium.com/p/7f7e929152f3">interactive</a> HTML content in compose?</h1>
""".trimIndent()
                            HtmlText(
                                modifier = Modifier.fillMaxWidth(),
                                html = htmlContent
                            )
                        }
                    }
                }
            }
        }
    }
}
