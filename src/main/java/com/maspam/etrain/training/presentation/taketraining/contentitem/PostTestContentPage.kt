package com.maspam.etrain.training.presentation.taketraining.contentitem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maspam.etrain.training.core.presentation.component.CustomButtonFieldLoad
import com.maspam.etrain.training.core.presentation.component.TopBarWithStartImage

@Composable
fun PostTestContentPage(modifier: Modifier = Modifier, onMenuCLicked : () -> Unit) {
    Scaffold(
        modifier = modifier
            .systemBarsPadding(),
        topBar = {
            TopBarWithStartImage(
                name = "Post Test"
            ) {
                onMenuCLicked()
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                item {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = 20.dp),
                                text = "Please choose the answer that best suits your personality !!!",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                            )
                            for (i in 1..3) {
                                Column(
                                    modifier = Modifier.padding(top = 15.dp)
                                ) {
                                    Text(
                                        text = "Literacy",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                    )
                                    Divider(
                                        thickness = 2.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 15.dp)
                                    )
                                    Text(
                                        text = "The question is pointed in literacy material, if you has understanding that material you can answer this question easily ",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal)
                                    )
                                }
                                for (e in 1..2) {
                                    Text(
                                        modifier = Modifier.padding(vertical = 15.dp),
                                        text = "$e. Question number $e ",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal)
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 10.dp),
                                        text = "Answer",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal)
                                    )
                                    TextField(
                                        modifier = Modifier
                                            .padding(vertical = 5.dp)
                                            .fillMaxWidth()
                                            .clip(shape = RoundedCornerShape(15.dp)),
                                        value = "sddsgnfdsgndsjgndfjsngdjsgdsnjgdnsjgndsjngdsgjdfngjdsnjgfndsjgndjsgndsgjdsngjfdsnjgdnsdsf",
                                        onValueChange = {

                                        },
                                        textStyle = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Normal,
                                            lineHeight = 20.sp
                                        ),
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(70.dp))
                        }
                    }
                }
            }
            CustomButtonFieldLoad(
                buttonName = "Submit",
                buttonColor = MaterialTheme.colorScheme.primary,
                isLoading = false,
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) { }
        }

    }
}
