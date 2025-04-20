package com.maspam.etrain.training.presentation.taketraining

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.ChevronsRight
import com.composables.icons.lucide.CirclePlay
import com.composables.icons.lucide.Lock
import com.composables.icons.lucide.LogOut
import com.composables.icons.lucide.Lucide
import com.maspam.etrain.training.core.presentation.utils.ToComposable
import com.maspam.etrain.training.core.presentation.utils.eventListener
import com.maspam.etrain.training.domain.model.EnrollModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.taketraining.contentitem.CommonContentPage
import com.maspam.etrain.training.presentation.taketraining.contentitem.ContentCertificatePage
import com.maspam.etrain.training.presentation.taketraining.contentitem.KaryaNyataContentPage
import com.maspam.etrain.training.presentation.taketraining.contentitem.PostTestContentPage
import com.maspam.etrain.training.presentation.taketraining.viewmodel.TakeTrainingViewModel
import kotlinx.coroutines.launch

@Composable
fun TakeTrainingPage(
    enrollModel: EnrollModel? = null,
    takeTrainingViewModel: TakeTrainingViewModel,
    navigateToLoginPage: () -> Unit,
    navBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    /**
     * Drawer
     */

    takeTrainingViewModel.setInitialEnrollModel(enrollModel = enrollModel ?: EnrollModel())

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()

    val state by takeTrainingViewModel.state.collectAsState()

    eventListener(
        takeTrainingViewModel.globalEvent
    ) { event ->
        when (event) {
            is GlobalEvent.Error -> {
                takeTrainingViewModel.setError(event.e)
            }
        }
    }

    state.error?.let {
        it.ToComposable(
            isLoading = state.isLoading,
            navigateToLoginPage = navigateToLoginPage,
            tryRequestAgain = {

            }
        ) {
            takeTrainingViewModel.setError(e = null)
        }
    }


    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    TrainingDrawerContent(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                        selectedTopic = state.topicSelection ?: 0,
                        selectedSection = state.sectionSelection ?: 0,
                        enrollModel = state.data ?: EnrollModel(),
                        navBack = navBack
                    ) { section, topic, lastIndex ->
                        if (lastIndex > 0) {
                            takeTrainingViewModel.setSelectedTopic(lastIndex, lastIndex)
                        }
                        scope.launch {
                            takeTrainingViewModel.setSelectedTopic(section, topic)
                            if (drawerState.isOpen) {
                                drawerState.close()
                            }
                        }
                    }
                }
            }
        ) {
            /**
             * Content from drawer
             */
            when (state.sectionSelection) {
                95 -> PostTestContentPage() {
                    scope.launch {
                        drawerState.open()
                    }
                }

                96 -> KaryaNyataContentPage() {
                    scope.launch {
                        drawerState.open()
                    }
                }

                97 -> ContentCertificatePage() {
                    scope.launch {
                        drawerState.open()
                    }
                }

                else -> CommonContentPage {
                    scope.launch {
                        drawerState.open()
                    }
                }
            }
        }
    }
}

@Composable
fun TrainingDrawerContent(
    modifier: Modifier = Modifier,
    selectedSection: Int,
    selectedTopic: Int,
    enrollModel: EnrollModel,
    navBack: () -> Unit,
    onDrawerClicked: (Int, Int, Int) -> Unit,
) {

    val a = enrollModel.pLearn ?: 0
    val b = enrollModel.sLearn ?: 0

    // Last index of section
    val sectionLast = enrollModel.trainingDetail?.sections?.lastIndex

    Column(
        modifier = modifier
            .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.Start,
    ) {
        enrollModel.trainingDetail?.sections?.forEachIndexed { i, dataSection ->

            // Last index of topic
            val topicLast = dataSection.topics?.lastIndex

            Row(
                modifier = Modifier.padding(top = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Lucide.ChevronsRight,
                    contentDescription = "Icon Arrow"
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = dataSection.name ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(
                    vertical = 13.dp
                ),
                thickness = 1.dp,
            )
            dataSection.topics?.forEachIndexed { e, dataTopic ->
                NavigationDrawerItem(
                    icon = {
                        if (i <= a) {
                            if (e < b || i < a) {
                                Icon(
                                    imageVector = Lucide.Check,
                                    contentDescription = "Check Icon",
                                    tint = Color.Green
                                )
                            } else if (b == e) {
                                Icon(
                                    imageVector = Lucide.CirclePlay,
                                    contentDescription = "Cirle Play Icon"
                                )
                            } else {
                                Icon(imageVector = Lucide.Lock, contentDescription = "Lock Icon")
                            }
                        } else {
                            Icon(imageVector = Lucide.Lock, contentDescription = "Lock Icon")
                        }
                    },
                    label = {
                        Text(
                            text = dataTopic.name ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Normal,
                            )
                        )
                    },
                    selected = i == selectedSection && e == selectedTopic,
                    onClick = {
                        if (i <= a) {
                            if (e < b || i < a) {
                                onDrawerClicked(i, e, 0)
                            } else {
                                if (b == e) {
                                    if (i == sectionLast) {
                                        if (e == topicLast) {
                                            onDrawerClicked(i, e, 95)
                                        }
                                    } else {
                                        onDrawerClicked(i, e, 0)
                                    }
                                } else {
                                    println("Lock")
                                }
                            }
                        } else {
                            println("Lock")
                        }
                    }
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 20.dp),
            thickness = 2.dp,
        )
        NavigationDrawerItem(
            icon = {
                if (a == 95) {
                    Icon(
                        imageVector = Lucide.CirclePlay,
                        contentDescription = "Circle Play Icon",
                    )
                } else if (a > 95) {
                    Icon(
                        imageVector = Lucide.Check,
                        contentDescription = "Check Icon",
                        tint = Color.Green
                    )
                } else {
                    Icon(imageVector = Lucide.Lock, contentDescription = "Lock Icon")
                }
            },
            label = {
                Text(
                    text = "Post Test",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
                )
            },
            selected = selectedSection == 95,
            onClick = {
                if (a < 95) {
                    println("lock")
                } else if (a == 95 || a > 95) {
                    onDrawerClicked(95, 95, 95)
                }
            }
        )
        NavigationDrawerItem(
            icon = {
                if (a == 96) {
                    Icon(
                        imageVector = Lucide.CirclePlay,
                        contentDescription = "Circle Play Icon",
                    )
                } else if (a > 96) {
                    Icon(
                        imageVector = Lucide.Check,
                        contentDescription = "Check Icon",
                        tint = Color.Green
                    )
                } else {
                    Icon(imageVector = Lucide.Lock, contentDescription = "Lock Icon")
                }
            },
            label = {
                Text(
                    text = "Karya Nyata",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
                )
            },
            selected = selectedSection == 96,
            onClick = {
                if (a < 96) {
                    println("lock")
                } else if (a == 96 || a > 96) {
                    onDrawerClicked(96, 96, 96)
                }
            }
        )
        NavigationDrawerItem(
            icon = {
                if (a == 97) {
                    Icon(
                        imageVector = Lucide.CirclePlay,
                        contentDescription = "Circle Play Icon",
                    )
                } else if (a > 97) {
                    Icon(
                        imageVector = Lucide.Check,
                        contentDescription = "Check Icon",
                        tint = Color.Green
                    )
                } else {
                    Icon(imageVector = Lucide.Lock, contentDescription = "Lock Icon")
                }
            },
            label = {
                Text(
                    text = "Certificate",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
                )
            },
            selected = selectedSection == 97,
            onClick = {
                if (a < 97) {
                    println("lock")
                } else if (a == 97 || a > 97) {
                    onDrawerClicked(97, 97, 97)
                }
            }
        )
        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = 13.dp
            ),
            thickness = 2.dp,
        )
        NavigationDrawerItem(
            icon = {
                Icon(imageVector = Lucide.LogOut, contentDescription = "Exit")
            },
            label = {
                Text(
                    text = "Exit",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
                )
            },
            selected = selectedSection == 101,
            onClick = {
                navBack()
            }
        )
    }
}


