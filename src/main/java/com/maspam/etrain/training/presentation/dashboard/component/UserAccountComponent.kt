package com.maspam.etrain.training.presentation.dashboard.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.maspam.etrain.R
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.domain.model.UserModel

@Composable
fun UserAccountComponent(
    userModel: UserModel,
    modifier: Modifier = Modifier,
    onReset: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = constructUrl(userModel.imageProfile ?: ""),
                    contentDescription = "user image",
                    error = painterResource(R.drawable.profile_default),
                    placeholder = painterResource(R.drawable.profile_default),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(shape = RoundedCornerShape(100))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = userModel.name ?: stringResource(R.string.username),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = userModel.nip ?: stringResource(R.string.email),
                        style = MaterialTheme.typography.bodySmall.copy( color = MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
            Text(
                text = "Reset",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Red),
                modifier = Modifier.clickable {
                    onReset(userModel.nip ?: "")
                },
            )
        }
    }
}