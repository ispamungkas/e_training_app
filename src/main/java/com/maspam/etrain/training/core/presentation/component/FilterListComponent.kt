package com.maspam.etrain.training.core.presentation.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FilterListComponent(
    scrollState: ScrollState = rememberScrollState(initial = 0),
    filters: List<String>,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var selectedIndex by remember { mutableStateOf(0) }

    Row(
        modifier = modifier
            .padding(vertical = 10.dp)
            .horizontalScroll(state = scrollState),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        filters.forEachIndexed { index, filter ->
            Box(
                modifier = Modifier
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(15.dp))
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(color = if (selectedIndex == index) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable {
                        selectedIndex = index
                        onSelected(filters[selectedIndex])
                    }
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 10.dp
                        ),
                    text = filter,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = if (selectedIndex == index)
                            MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
    }
}