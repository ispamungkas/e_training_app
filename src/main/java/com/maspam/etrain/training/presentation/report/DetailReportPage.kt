package com.maspam.etrain.training.presentation.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.maspam.etrain.R
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.presentation.component.ListEnrollTrainingComponentWithGrade
import com.maspam.etrain.training.domain.model.UserModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailReportPage(
    modifier: Modifier = Modifier,
    userModel: UserModel
) {

    Scaffold(
        modifier = modifier.systemBarsPadding(),
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    userModel.imageProfile?.let { profile ->
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
                    Text(text = userModel.name ?: "Username", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp))
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = userModel.phoneNumber ?: "0888888888", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal))

                }
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Enrolled Training", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal))
                }
            }

            userModel.enrolls?.let {
                items(it, key = { enrollData -> enrollData.id ?: 0 }) { enrollData ->
                    ListEnrollTrainingComponentWithGrade(
                        modifier = Modifier.fillMaxWidth(),
                        data = enrollData,
                        id = enrollData.id,
                        typeTraining = enrollData.trainingDetail?.typeTraining,
                        status = enrollData.status,
                        typeTrainingCategory = enrollData.trainingDetail?.typeTrainingCategory,
                        nameTraining = enrollData.trainingDetail?.name,
                        due = enrollData.outDate,
                        imageUri = enrollData.trainingDetail?.image,
                        grade = enrollData.grade
                    )
                }
            } ?: item {
                Text(
                    text = "Doen't have any enroll training",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                )
            }
        }
    }
}