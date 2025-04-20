package com.maspam.etrain.training.core.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.composables.icons.lucide.ChevronLeft
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Menu
import com.maspam.etrain.R

@Composable
fun TopBarComponent(
    imageUri: String? = null,
    name: String? = "",
    modifier: Modifier = Modifier,
    onProfileClicked: () -> Unit,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUri ?: R.drawable.profile_default,
                contentDescription = "profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = RoundedCornerShape(100))
                    .clickable {
                        onProfileClicked()
                    },
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "halo", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = "Mr ${name ?: stringResource(R.string.user)}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }

    }
}

@Composable
fun TopBarWithStartImage(
    name: String = "",
    modifier: Modifier = Modifier,
    onIconClicked: () -> Unit,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 10.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.app_info_image),
            contentDescription = "Icon App",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(shape = RoundedCornerShape(100))
                .clickable {
                    onIconClicked()
                },
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun TopBarWithHumbergerIcon(
    name: String = "",
    modifier: Modifier = Modifier,
    onIconClicked: () -> Unit,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 10.dp)
    ) {
        Icon(
            imageVector = Lucide.Menu,
            contentDescription = "Icon App",
            modifier = Modifier
                .clip(shape = RoundedCornerShape(100))
                .clickable {
                    onIconClicked()
                },
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun TopBarWithHumbergerIconSingle(
    modifier: Modifier = Modifier,
    onIconClicked: () -> Unit,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 10.dp)
    ) {
        Icon(
            imageVector = Lucide.Menu,
            contentDescription = "Icon App",
            modifier = Modifier
                .clip(shape = RoundedCornerShape(100))
                .clickable {
                    onIconClicked()
                },
        )
    }
}

@Composable
fun TopBarWithArrowComponent(
    section: String? = "",
    modifier: Modifier = Modifier,
    onBackPress: () -> Unit,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 10.dp),
    ) {
        Icon(
            Lucide.ChevronLeft,
            contentDescription = "Back",
            modifier = Modifier
                .size(24.dp)
                .clip(shape = RoundedCornerShape(100))
                .clickable { onBackPress() })
        Spacer(modifier = Modifier.width(25.dp))
        Text(
            text = section ?: "",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Preview
@Composable
fun Check(modifier: Modifier = Modifier) {
    TopBarWithHumbergerIcon(
        name = "Post Test"
    ) {  }
}