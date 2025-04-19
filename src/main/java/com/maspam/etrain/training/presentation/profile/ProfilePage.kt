package com.maspam.etrain.training.presentation.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lock
import com.composables.icons.lucide.LogOut
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.User
import com.maspam.etrain.R
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.presentation.component.ShimmerEffect
import com.maspam.etrain.training.core.presentation.component.TopBarWithArrowComponent
import com.maspam.etrain.training.domain.model.UserModel
import com.maspam.etrain.training.presentation.profile.component.CardTotalJPComponent
import com.maspam.etrain.training.presentation.profile.component.MenuProfileComponent
import com.maspam.etrain.training.presentation.profile.viewmodel.ProfileViewModel

@Composable
fun ProfilePage(
    profileViewModel: ProfileViewModel,
    scrollState: ScrollState = rememberScrollState(initial = 0),
    modifier: Modifier = Modifier,
    navigateToListTraining: () -> Unit,
    navigateToEditProfile: (UserModel?) -> Unit,
    navigateToChangePasswordProfile: (String) -> Unit,
    navigateToInfo: () -> Unit,
    onBackPress: () -> Unit,
    navigateToLogin: () -> Unit
) {

    val state by profileViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopBarWithArrowComponent(
                section = stringResource(R.string.my_account),
                onBackPress = onBackPress
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(innerPadding)
                .verticalScroll(state = scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 23.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.isLoading) {
                    ShimmerEffect(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(shape = RoundedCornerShape(100))
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    ShimmerEffect(
                        modifier = Modifier
                            .width(150.dp)
                            .height(20.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    ShimmerEffect(
                        modifier = Modifier
                            .width(100.dp)
                            .height(20.dp)
                    )
                } else {
                    state.user?.imageProfile?.let { profile ->
                        AsyncImage(
                            model = constructUrl(profile),
                            contentDescription = "Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(shape = RoundedCornerShape(100))
                        )
                    } ?: Image(
                        painter = painterResource(R.drawable.profile_default),
                        contentDescription = "Image Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(shape = RoundedCornerShape(100))
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = state.user?.name ?: "Username", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp))
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = state.user?.phoneNumber ?: "0888888888", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal))
                }
            }
            if (state.isLoading) {
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(horizontal = 20.dp, vertical = 23.dp)
                )
            } else {
                AnimatedVisibility(
                    true
                ) {
                    CardTotalJPComponent(
                        totalJp = state.totalJp,
                        trainJp = state.trainJp,
                        workshopJp = state.workShopJp,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical =  23.dp)
                    ) {
                        navigateToListTraining()
                    }
                }
            }
            MenuProfileComponent(
                imageVector = Lucide.User,
                name = stringResource(R.string.edit_profile),
            ) {
                navigateToEditProfile(
                    state.user
                )
            }
            MenuProfileComponent(
                imageVector = Lucide.Lock,
                name = stringResource(R.string.change_password),
            ) {
                navigateToChangePasswordProfile(state.user?.nip ?: "")
            }
            MenuProfileComponent(
                imageVector = Lucide.Info,
                name = stringResource(R.string.information),
            ) {
                navigateToInfo()
            }
            Divider(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
            MenuProfileComponent(
                imageVector = Lucide.LogOut,
                name = stringResource(R.string.log_out),
                color = MaterialTheme.colorScheme.error,
            ) {
                profileViewModel.removeSession()
                navigateToLogin()
            }
        }

    }
}
