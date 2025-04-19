package com.maspam.etrain.training.presentation.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maspam.etrain.training.core.presentation.component.Menu
import com.maspam.etrain.training.core.presentation.component.bounceClickWithBackgroundEffect

@Composable
fun MenusComponent(
    menus: List<Menu>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier.padding(horizontal = 38.dp),
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        items(menus) { menu ->
            MenuItem(menu)
        }
    }
}

@Composable
fun MenuItem(
    menu: Menu,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .border(width = 2.dp, color = MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(15.dp))
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = MaterialTheme.colorScheme.onBackground)
                .bounceClickWithBackgroundEffect()
        ) {
            Icon(
                imageVector = menu.imageVector,
                contentDescription = "Icon Menu",
                modifier = Modifier.size(24.dp).align(alignment = Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = menu.name,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal)
        )
    }
}
