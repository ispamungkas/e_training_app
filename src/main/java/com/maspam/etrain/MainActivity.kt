package com.maspam.etrain

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.maspam.etrain.training.core.presentation.utils.AddNewsScreen
import com.maspam.etrain.training.core.presentation.utils.ChangePasswordProfileScreen
import com.maspam.etrain.training.core.presentation.utils.ChangePasswordScreen
import com.maspam.etrain.training.core.presentation.utils.CustomNavType
import com.maspam.etrain.training.core.presentation.utils.DetailEnrollTrainingScreen
import com.maspam.etrain.training.core.presentation.utils.DetailKaryaNyataScreen
import com.maspam.etrain.training.core.presentation.utils.DetailNewsScreen
import com.maspam.etrain.training.core.presentation.utils.DetailReportScreen
import com.maspam.etrain.training.core.presentation.utils.DetailTrainingKNScreen
import com.maspam.etrain.training.core.presentation.utils.DetailTrainingScreen
import com.maspam.etrain.training.core.presentation.utils.EditNewsScreen
import com.maspam.etrain.training.core.presentation.utils.EditProfileScreen
import com.maspam.etrain.training.core.presentation.utils.ForgotPasswordScreen
import com.maspam.etrain.training.core.presentation.utils.FormAddPostTestScreen
import com.maspam.etrain.training.core.presentation.utils.FormAddTrainingScreen
import com.maspam.etrain.training.core.presentation.utils.FormEditTrainingScreen
import com.maspam.etrain.training.core.presentation.utils.FormUpdateTopicScreen
import com.maspam.etrain.training.core.presentation.utils.HeadSchoolDashboardScreen
import com.maspam.etrain.training.core.presentation.utils.InformationScreen
import com.maspam.etrain.training.core.presentation.utils.InitialScreen
import com.maspam.etrain.training.core.presentation.utils.KabidDashboardScreen
import com.maspam.etrain.training.core.presentation.utils.KabidListNewsScreen
import com.maspam.etrain.training.core.presentation.utils.ListEnrollTrainingProfileScreen
import com.maspam.etrain.training.core.presentation.utils.ListNewsScreen
import com.maspam.etrain.training.core.presentation.utils.ListOpenTrainingScreen
import com.maspam.etrain.training.core.presentation.utils.ListPostTestScreen
import com.maspam.etrain.training.core.presentation.utils.ListSectionScreen
import com.maspam.etrain.training.core.presentation.utils.ListTopicScreen
import com.maspam.etrain.training.core.presentation.utils.ListTrainingKNScreen
import com.maspam.etrain.training.core.presentation.utils.ListTrainingScreen
import com.maspam.etrain.training.core.presentation.utils.ListUserAccountScreen
import com.maspam.etrain.training.core.presentation.utils.LoginScreen
import com.maspam.etrain.training.core.presentation.utils.OTPRequestProfileScreen
import com.maspam.etrain.training.core.presentation.utils.OTPScreen
import com.maspam.etrain.training.core.presentation.utils.OTPValidateProfileScreen
import com.maspam.etrain.training.core.presentation.utils.ProfileScreen
import com.maspam.etrain.training.core.presentation.utils.RegisterScreen
import com.maspam.etrain.training.core.presentation.utils.Report
import com.maspam.etrain.training.core.presentation.utils.ScannerScreen
import com.maspam.etrain.training.core.presentation.utils.SuperUserDashboardScreen
import com.maspam.etrain.training.core.presentation.utils.TakeTrainingScreen
import com.maspam.etrain.training.core.presentation.utils.TeacherDashboardScreen
import com.maspam.etrain.training.domain.model.EnrollModel
import com.maspam.etrain.training.domain.model.NewsModel
import com.maspam.etrain.training.domain.model.PostTestModel
import com.maspam.etrain.training.domain.model.SectionModel
import com.maspam.etrain.training.domain.model.TopicModel
import com.maspam.etrain.training.domain.model.TrainingModel
import com.maspam.etrain.training.domain.model.UserModel
import com.maspam.etrain.training.presentation.authentication.ChangePasswordPage
import com.maspam.etrain.training.presentation.authentication.ForgotPasswordPage
import com.maspam.etrain.training.presentation.authentication.LoginPage
import com.maspam.etrain.training.presentation.authentication.OTPAction
import com.maspam.etrain.training.presentation.authentication.OTPPage
import com.maspam.etrain.training.presentation.authentication.RegisterPage
import com.maspam.etrain.training.presentation.authentication.viewmodel.AuthenticationViewModel
import com.maspam.etrain.training.presentation.authentication.viewmodel.ChangePasswordViewModel
import com.maspam.etrain.training.presentation.authentication.viewmodel.ForgotPasswordViewModel
import com.maspam.etrain.training.presentation.authentication.viewmodel.OTPViewModel
import com.maspam.etrain.training.presentation.dashboard.HeadSchoolDashboardPage
import com.maspam.etrain.training.presentation.dashboard.KabidDashboardPage
import com.maspam.etrain.training.presentation.dashboard.SuperUserDashboardPage
import com.maspam.etrain.training.presentation.dashboard.TeacherDashboardPage
import com.maspam.etrain.training.presentation.dashboard.extended.ListOpenTrainingPage
import com.maspam.etrain.training.presentation.dashboard.extended.ListUserAccountPage
import com.maspam.etrain.training.presentation.dashboard.viewmodel.DashboardViewModel
import com.maspam.etrain.training.presentation.dashboard.viewmodel.ListOpenTrainingViewModel
import com.maspam.etrain.training.presentation.dashboard.viewmodel.UserAccountViewModel
import com.maspam.etrain.training.presentation.enroll.DetailEnrollTraining
import com.maspam.etrain.training.presentation.enroll.ListEnrollTrainingProfilePage
import com.maspam.etrain.training.presentation.enroll.viewmodel.EnrollViewModel
import com.maspam.etrain.training.presentation.enroll.viewmodel.ListEnrollProfileViewModel
import com.maspam.etrain.training.presentation.karyanyata.DetailKaryaNyataPage
import com.maspam.etrain.training.presentation.karyanyata.DetailTrainingKNPage
import com.maspam.etrain.training.presentation.karyanyata.ListTrainingKNPage
import com.maspam.etrain.training.presentation.karyanyata.viewmodel.KaryaNyataViewModel
import com.maspam.etrain.training.presentation.news.DetailNewsPage
import com.maspam.etrain.training.presentation.news.FormAddNewsPage
import com.maspam.etrain.training.presentation.news.FormEditNewsPage
import com.maspam.etrain.training.presentation.news.KabidListNewsPage
import com.maspam.etrain.training.presentation.news.ListNewsPage
import com.maspam.etrain.training.presentation.news.viewmodel.ListNewsViewModel
import com.maspam.etrain.training.presentation.news.viewmodel.NewsViewModel
import com.maspam.etrain.training.presentation.profile.ChangeDataProfilePage
import com.maspam.etrain.training.presentation.profile.ChangePasswordProfilePage
import com.maspam.etrain.training.presentation.profile.InformationPage
import com.maspam.etrain.training.presentation.profile.ProfilePage
import com.maspam.etrain.training.presentation.profile.RequestOtpPage
import com.maspam.etrain.training.presentation.profile.VerificationOtpPage
import com.maspam.etrain.training.presentation.profile.viewmodel.ProfileViewModel
import com.maspam.etrain.training.presentation.profile.viewmodel.UpdateDataProfileViewModel
import com.maspam.etrain.training.presentation.profile.viewmodel.UpdatePasswordProfileViewModel
import com.maspam.etrain.training.presentation.report.DetailReportPage
import com.maspam.etrain.training.presentation.report.ReportPage
import com.maspam.etrain.training.presentation.report.ReportViewModel
import com.maspam.etrain.training.presentation.scanner.ScannerPage
import com.maspam.etrain.training.presentation.scanner.ScannerViewModel
import com.maspam.etrain.training.presentation.taketraining.TakeTrainingPage
import com.maspam.etrain.training.presentation.taketraining.viewmodel.TakeTrainingViewModel
import com.maspam.etrain.training.presentation.training.DetailTrainingPage
import com.maspam.etrain.training.presentation.training.FormAddPostTest
import com.maspam.etrain.training.presentation.training.FormAddTrainingPage
import com.maspam.etrain.training.presentation.training.FormEditTopicPage
import com.maspam.etrain.training.presentation.training.FormEditTrainingPage
import com.maspam.etrain.training.presentation.training.ListPostTestPage
import com.maspam.etrain.training.presentation.training.ListSectionPage
import com.maspam.etrain.training.presentation.training.ListTopicPage
import com.maspam.etrain.training.presentation.training.ListTrainingPage
import com.maspam.etrain.training.presentation.training.viewmodel.DetailTrainingViewModel
import com.maspam.etrain.training.presentation.training.viewmodel.ListSectionViewModel
import com.maspam.etrain.training.presentation.training.viewmodel.ListTopicViewModel
import com.maspam.etrain.training.presentation.training.viewmodel.PostTestViewModel
import com.maspam.etrain.training.presentation.training.viewmodel.TrainingViewModel
import com.maspam.etrain.ui.theme.ETrainingTheme
import org.koin.androidx.compose.koinViewModel
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {

    var result = false

    private val cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            result= true
        } else {
            Toast.makeText(this, "Permission Decline", Toast.LENGTH_SHORT).show()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ETrainingTheme(
                dynamicColor = false
            ) {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = InitialScreen
                    ) {
                        composable<InitialScreen> {
                            InitialPage(
                                authenticationViewModel = koinViewModel<AuthenticationViewModel>(),
                                hasUser = {
                                    when(it) {
                                        "Super User" -> {
                                            navController.navigate(SuperUserDashboardScreen) {
                                                popUpTo(InitialScreen) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                        "Head School" -> {
                                            navController.navigate(HeadSchoolDashboardScreen) {
                                                popUpTo(InitialScreen) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                        "Kepala Bidang" -> {
                                            navController.navigate(KabidDashboardScreen) {
                                                popUpTo(InitialScreen) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                        "Teacher" -> {
                                            navController.navigate(TeacherDashboardScreen) {
                                                popUpTo(InitialScreen) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                        else -> {
                                            navController.navigate(LoginScreen) {
                                                popUpTo(InitialScreen) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                },
                                login = {
                                    navController.navigate(LoginScreen)
                                },
                            )
                        }

                        composable<LoginScreen> {
                            LoginPage(
                                authenticationViewModel = koinViewModel<AuthenticationViewModel>(),
                                onNavigateToForgotPasswordScreen = {
                                    navController.navigate(
                                        ForgotPasswordScreen
                                    )
                                },
                                onLoginSuccess = { userModel ->
                                    when (userModel.role) {
                                        "Super User" -> {
                                            navController.navigate(SuperUserDashboardScreen) {
                                                popUpTo(LoginScreen) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                        "Head School" -> {
                                            navController.navigate(HeadSchoolDashboardScreen) {
                                                popUpTo(LoginScreen) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                        "Teacher" -> {
                                            navController.navigate(TeacherDashboardScreen) {
                                                popUpTo(LoginScreen) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                        "Kepala Bidang" -> {
                                            navController.navigate(KabidDashboardScreen) {
                                                popUpTo(LoginScreen) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                        else -> {
                                            println("tidak ada")
                                        }
                                    }
                                },
                                navigateToLogin = {},
                            )
                        }
                        composable<RegisterScreen> {
                            RegisterPage(
                                authenticationViewModel = koinViewModel<AuthenticationViewModel>(),
                                navigateToLogin = { navController.navigate(LoginScreen) },
                                onRegisterSuccess = { navController.navigateUp() }
                            )
                        }
                        composable<ForgotPasswordScreen> {
                            ForgotPasswordPage(
                                forgotPasswordViewModel = koinViewModel<ForgotPasswordViewModel>(),
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                },
                                onNextPage = { navController.navigate(OTPScreen(nip = it)) }
                            )
                        }
                        composable<OTPScreen> {
                            val args = it.toRoute<OTPScreen>()

                            val otpViewModel = koinViewModel<OTPViewModel>()
                            val state by otpViewModel.state.collectAsStateWithLifecycle()
                            val focusRequesters = remember {
                                List(4) { FocusRequester() }
                            }
                            val focusManager = LocalFocusManager.current
                            val keyboardManager = LocalSoftwareKeyboardController.current

                            LaunchedEffect(state.focusIndex) {
                                state.focusIndex?.let { index ->
                                    focusRequesters.getOrNull(index)?.requestFocus()
                                }
                            }

                            LaunchedEffect(state.code, keyboardManager) {
                                val allNumberEntered = state.code.none { it == null }
                                if (allNumberEntered) {
                                    focusRequesters.forEach {
                                        it.freeFocus()
                                    }
                                    focusManager.clearFocus()
                                    keyboardManager?.hide()
                                    otpViewModel.verifyOtp(otp = state.code.joinToString(""))
                                }
                            }

                            OTPPage(
                                state = state,
                                onAction = { action ->
                                    when (action) {
                                        is OTPAction.OnEnterNumber -> {
                                            if (action.number != null) {
                                                focusRequesters[action.index].freeFocus()
                                            }
                                        }

                                        else -> Unit
                                    }
                                    otpViewModel.onAction(action)
                                },
                                focusRequesters = focusRequesters,
                                otpViewModel = otpViewModel,
                                onNextPage = {
                                    navController.navigate(ChangePasswordScreen(nip = args.nip))
                                },
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                },
                                modifier = Modifier
                            )
                        }
                        composable<ChangePasswordScreen> {
                            val args = it.toRoute<ChangePasswordScreen>()
                            ChangePasswordPage(
                                nip = args.nip,
                                changePasswordViewModel = koinViewModel<ChangePasswordViewModel>(),
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = true
                                        }
                                    }
                                },
                                navigateToNextPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                }
                            )
                        }
                        composable<SuperUserDashboardScreen> {
                            SuperUserDashboardPage(
                                dashboardViewModel = koinViewModel<DashboardViewModel>(),
                                profileViewModel = koinViewModel<ProfileViewModel>(),
                                navigateToListUserAccountPage = {
                                    navController.navigate(ListUserAccountScreen)
                                },
                                navigateToListNews = {
                                    navController.navigate(ListNewsScreen)
                                },
                                navigateToDetailNews = {
                                    navController.navigate(
                                        DetailNewsScreen(
                                            newsModel = it
                                        )
                                    )
                                },
                                navigateToScannerPage = {
                                    navController.navigate(ScannerScreen)
                                },
                                navigateToLoginPage = {
                                    navController.navigate(InitialScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = true
                                        }
                                    }
                                },
                            )
                        }
                        composable<TeacherDashboardScreen> {
                            TeacherDashboardPage(
                                modifier = Modifier.padding(innerPadding),
                                dashboardViewModel = koinViewModel<DashboardViewModel>(),
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(TeacherDashboardScreen) {
                                            inclusive = true
                                        }
                                    }
                                },
                                onProfileClicked = {
                                    navController.navigate(ProfileScreen)
                                },
                                navigateToEnrollList = {
                                    navController.navigate(ListEnrollTrainingProfileScreen)
                                },
                                navigateToListOpenTraining = {
                                    navController.navigate(ListOpenTrainingScreen)
                                },
                                navigateToListNews = {
                                    navController.navigate(ListNewsScreen)
                                },
                                navigateToDetailNews = {
                                    navController.navigate(
                                        DetailNewsScreen(
                                            newsModel = it
                                        )
                                    )
                                },
                                navigateToScannerPage = {
                                    navController.navigate(ScannerScreen)
                                }
                            )
                        }
                        composable<HeadSchoolDashboardScreen> {
                            HeadSchoolDashboardPage(
                                dashboardViewModel = koinViewModel<DashboardViewModel>(),
                                onProfileClicked = {
                                    navController.navigate(ProfileScreen)
                                },
                                navigateToEnrollList = {
                                    navController.navigate(ListEnrollTrainingProfileScreen)
                                },
                                navigateToListOpenTraining = {
                                    navController.navigate(ListOpenTrainingScreen)
                                },
                                navigateToListNews = {
                                    navController.navigate(ListNewsScreen)
                                },
                                navigateToDetailNews = {
                                    navController.navigate(
                                        DetailNewsScreen(
                                            newsModel = it
                                        )
                                    )
                                },
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(InitialScreen) {
                                            inclusive = true
                                        }
                                    }
                                },
                                navigateToReport = {
                                    navController.navigate(Report)
                                },
                                navigateToKaryaNyataManajement = {
                                    navController.navigate(ListTrainingKNScreen)
                                },
                                navigateToScannerPage = {
                                    navController.navigate(ScannerScreen)
                                }
                            )
                        }
                        composable<KabidDashboardScreen> {
                            KabidDashboardPage(
                                dashboardViewModel = koinViewModel<DashboardViewModel>(),
                                onProfileClicked = {
                                    navController.navigate(ProfileScreen)
                                },
                                navigateToEnrollList = {
                                    navController.navigate(ListEnrollTrainingProfileScreen)
                                },
                                navigateToListOpenTraining = {
                                    navController.navigate(ListOpenTrainingScreen)
                                },
                                navigateToListNews = {
                                    navController.navigate(ListNewsScreen)
                                },
                                navigateToDetailNews = {
                                    navController.navigate(
                                        DetailNewsScreen(
                                            newsModel = it
                                        )
                                    )
                                },
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(InitialScreen) {
                                            inclusive = true
                                        }
                                    }
                                },
                                navigateToListTrainingManajement = {
                                    navController.navigate(ListTrainingScreen)
                                },
                                navigateToKabidListNews = {
                                    navController.navigate(KabidListNewsScreen)
                                },
                                navigateToScannerPage = {
                                    navController.navigate(ScannerScreen)
                                }
                            )
                        }
                        composable<ListOpenTrainingScreen> {
                            ListOpenTrainingPage(
                                modifier = Modifier.padding(innerPadding),
                                listOpenTrainingViewModel = koinViewModel<ListOpenTrainingViewModel>(),
                                onBackPressed = {
                                    navController.navigateUp()
                                },
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(TeacherDashboardScreen) {
                                            inclusive = true
                                        }
                                    }
                                },
                                navigateToEnrollList = {
                                    navController.navigate(ListEnrollTrainingProfileScreen)
                                }
                            )
                        }
                        composable<ProfileScreen> {
                            ProfilePage(
                                modifier = Modifier.padding(innerPadding),
                                profileViewModel = koinViewModel<ProfileViewModel>(),
                                navigateToEditProfile = { data ->
                                    navController.navigate(
                                        EditProfileScreen(
                                            userModel = data ?: UserModel()
                                        )
                                    )
                                },
                                navigateToChangePasswordProfile = {
                                    navController.navigate(
                                        OTPRequestProfileScreen(
                                            nip = it
                                        )
                                    )
                                },
                                navigateToInfo = {
                                    navController.navigate(InformationScreen)
                                },
                                onBackPress = {
                                    navController.navigateUp()
                                },
                                navigateToLogin = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(InitialScreen) {
                                            inclusive = true
                                        }
                                    }
                                },
                                navigateToListTraining = {
                                    navController.navigate(
                                        ListEnrollTrainingProfileScreen
                                    )
                                }
                            )
                        }
                        composable<InformationScreen> {
                            InformationPage(
                                onBackPressed = {
                                    navController.navigateUp()
                                }
                            )
                        }

                        composable<EditProfileScreen>(
                            typeMap = mapOf(
                                typeOf<UserModel>() to CustomNavType.userModelType
                            )
                        ) {
                            val data = it.toRoute<EditProfileScreen>()
                            ChangeDataProfilePage(
                                userModel = data.userModel,
                                changeUpdateDataProfileViewModel = koinViewModel<UpdateDataProfileViewModel>(),
                                onBackPressed = {
                                    navController.navigateUp()
                                },
                                onUpdateSuccess = {
                                    navController.navigate(ProfileScreen) {
                                        popUpTo(ProfileScreen) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }

                        composable<OTPRequestProfileScreen> {
                            val data = it.toRoute<OTPRequestProfileScreen>()
                            RequestOtpPage(
                                nip = data.nip,
                                forgotPasswordViewModel = koinViewModel<ForgotPasswordViewModel>(),
                                onNextPage = {
                                    navController.navigate(OTPValidateProfileScreen)
                                },
                                navigateToLoginPage = {
                                    navController.navigate(ProfileScreen) {
                                        popUpTo(ProfileScreen) {
                                            inclusive = true
                                        }
                                    }
                                },
                                onBackPressed = {
                                    navController.navigateUp()
                                }
                            )
                        }

                        composable<ChangePasswordProfileScreen> {
                            ChangePasswordProfilePage(
                                updatePasswordProfileViewModel = koinViewModel<UpdatePasswordProfileViewModel>(),
                                onSuccessUpdate = {
                                    navController.navigate(ProfileScreen) {
                                        popUpTo(ProfileScreen) {
                                            inclusive = true
                                        }
                                    }
                                },
                                navigateToLoginPage = {
                                    navController.navigate(ProfileScreen) {
                                        popUpTo(ProfileScreen) {
                                            inclusive = true
                                        }
                                    }
                                },
                                onBackPressed = {
                                    navController.navigateUp()
                                }
                            )
                        }

                        composable<OTPValidateProfileScreen> {

                            val otpViewModel = koinViewModel<OTPViewModel>()
                            val state by otpViewModel.state.collectAsStateWithLifecycle()
                            val focusRequesters = remember {
                                List(4) { FocusRequester() }
                            }
                            val focusManager = LocalFocusManager.current
                            val keyboardManager = LocalSoftwareKeyboardController.current

                            LaunchedEffect(state.focusIndex) {
                                state.focusIndex?.let { index ->
                                    focusRequesters.getOrNull(index)?.requestFocus()
                                }
                            }

                            LaunchedEffect(state.code, keyboardManager) {
                                val allNumberEntered = state.code.none { code ->
                                    code == null
                                }
                                if (allNumberEntered) {
                                    focusRequesters.forEach { fr ->
                                        fr.freeFocus()
                                    }
                                    focusManager.clearFocus()
                                    keyboardManager?.hide()
                                    otpViewModel.verifyOtp(otp = state.code.joinToString(""))
                                }
                            }

                            VerificationOtpPage(
                                state = state,
                                onAction = { action ->
                                    when (action) {
                                        is OTPAction.OnEnterNumber -> {
                                            if (action.number != null) {
                                                focusRequesters[action.index].freeFocus()
                                            }
                                        }

                                        else -> Unit
                                    }
                                    otpViewModel.onAction(action)
                                },
                                focusRequesters = focusRequesters,
                                otpViewModel = otpViewModel,
                                onNextPage = {
                                    navController.navigate(ChangePasswordProfileScreen)
                                },
                                navigateToLoginPage = {
                                    navController.navigate(ProfileScreen) {
                                        popUpTo(ProfileScreen) {
                                            inclusive = true
                                        }
                                    }
                                },
                                onBackPressed = {
                                    navController.navigateUp()
                                },
                            )
                        }

                        composable<ListEnrollTrainingProfileScreen> {
                            ListEnrollTrainingProfilePage(
                                modifier = Modifier.padding(innerPadding),
                                listEnrollProfileViewModel = koinViewModel<ListEnrollProfileViewModel>(),
                                onBackPressed = {
                                    navController.navigateUp()
                                },
                                onItemClick = {
                                    navController.navigate(DetailEnrollTrainingScreen(it))
                                },
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                }
                            )
                        }

                        composable<DetailEnrollTrainingScreen>(
                            typeMap = mapOf(
                                typeOf<EnrollModel>() to CustomNavType.enrollModel
                            )
                        ) {
                            val data = it.toRoute<DetailEnrollTrainingScreen>()
                            DetailEnrollTraining(
                                modifier = Modifier.padding(innerPadding),
                                enroll = data.enrolModel,
                                enrollViewModel = koinViewModel<EnrollViewModel>(),
                                onBackPressed = {
                                    navController.navigateUp()
                                },
                                navigateToTakeTraining = {
                                    navController.navigate(
                                        TakeTrainingScreen(
                                            enrollModel = data.enrolModel
                                        )
                                    )
                                }
                            )
                        }

                        composable<ListNewsScreen> {
                            ListNewsPage(
                                listNewsViewModel = koinViewModel<ListNewsViewModel>(),
                                navigateToDetailNews = {
                                    navController.navigate(DetailNewsScreen(it))
                                },
                                onBackPressed = {
                                    navController.navigateUp()
                                }
                            )
                        }

                        composable<DetailNewsScreen>(
                            typeMap = mapOf(
                                typeOf<NewsModel>() to CustomNavType.newsModel
                            )
                        ) {
                            val data = it.toRoute<DetailNewsScreen>().newsModel
                            DetailNewsPage(
                                newsImage = data.image ?: "",
                                newsName = data.name ?: "",
                                newsContent = data.desc ?: "",
                                newsAuthor = data.author ?: "",
                                newsPublish = data.publishDate ?: 0L,
                                onBackPressed = {
                                    navController.navigateUp()
                                },
                            )
                        }

                        composable<TakeTrainingScreen>(
                            typeMap = mapOf(
                                typeOf<EnrollModel>() to CustomNavType.enrollModel
                            )
                        ) {
                            val data = it.toRoute<TakeTrainingScreen>().enrollModel
                            TakeTrainingPage(
                                enrollModel = data,
                                takeTrainingViewModel = koinViewModel<TakeTrainingViewModel>(),
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                },
                                navBack = {
                                    navController.navigateUp()
                                }
                            )
                        }

                        composable<ListTrainingScreen> {
                            ListTrainingPage(
                                trainingViewModel = koinViewModel<TrainingViewModel>(),
                                navigateToFormAddTraining = {
                                    navController.navigate(FormAddTrainingScreen)
                                },
                                onBackPressed = {
                                    navController.navigateUp()
                                },
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                }
                            ) { training ->
                                navController.navigate(
                                    DetailTrainingScreen(trainingModel = training)
                                )
                            }
                        }

                        composable<DetailTrainingScreen>(
                            typeMap = mapOf(
                                typeOf<TrainingModel>() to CustomNavType.trainingModel
                            )
                        ) {
                            val training = it.toRoute<DetailTrainingScreen>().trainingModel
                            DetailTrainingPage(
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                },
                                training = training,
                                navigateToListSection = { listSection, trainingId->
                                    navController.navigate(
                                        ListSectionScreen(
                                            section = listSection,
                                            trainingId = trainingId,
                                        )
                                    )
                                },
                                navigateToEditPage = { value ->
                                    navController.navigate(FormEditTrainingScreen(value))
                                },
                                detailTrainingViewModel = koinViewModel<DetailTrainingViewModel>(),
                                navigateToPostTestPage = { listSection, trainId, listPostTest->
                                    navController.navigate(ListPostTestScreen(
                                        trainingId = trainId, section = listSection, postTests = listPostTest
                                    ))
                                }
                            )
                        }

                        composable<FormAddTrainingScreen> {
                            FormAddTrainingPage(
                                trainingViewModel = koinViewModel<TrainingViewModel>()
                            ) {
                                navController.navigateUp()
                            }
                        }

                        composable<FormEditTrainingScreen>(
                            typeMap = mapOf(
                                typeOf<TrainingModel>() to CustomNavType.trainingModel
                            )
                        ) {
                            val data = it.toRoute<FormEditTrainingScreen>().trainingModel
                            FormEditTrainingPage(
                                trainingModel = data,
                                trainingViewModel = koinViewModel<TrainingViewModel>(),
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                }
                            ) {
                                navController.navigateUp()
                            }
                        }

                        composable<ListSectionScreen>(
                            typeMap = mapOf(
                                typeOf<List<SectionModel>>() to CustomNavType.listSectionModel
                            )
                        ) {
                            val data = it.toRoute<ListSectionScreen>()
                            ListSectionPage(
                                listSectionViewModel = koinViewModel<ListSectionViewModel>(),
                                trainingId = data.trainingId,
                                section = data.section,
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                },
                                navigateToTopicPage = { listTopic, sectionId ->
                                    navController.navigate(
                                        ListTopicScreen(
                                            sectionId = sectionId,
                                            topicList = listTopic
                                        )
                                    )
                                },
                                onBackPressed = {
                                    navController.navigateUp()
                                }
                            )
                        }

                        composable<ListTopicScreen>(
                            typeMap = mapOf(
                                typeOf<List<TopicModel>>() to CustomNavType.listTopicModel
                            )
                        ) {
                            val data = it.toRoute<ListTopicScreen>()
                            ListTopicPage(
                                listTopicViewModel = koinViewModel<ListTopicViewModel>(),
                                topicList = data.topicList,
                                sectionId = data.sectionId,
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                },
                                navigateToTopicUpdateScreen = {
                                    navController.navigate(FormUpdateTopicScreen(topicModel = it))
                                }
                            ) {
                                navController.navigateUp()
                            }
                        }

                        composable<FormUpdateTopicScreen>(
                            typeMap = mapOf(
                                typeOf<TopicModel>() to CustomNavType.topicModel
                            )
                        ) {
                            val data = it.toRoute<FormUpdateTopicScreen>()
                            FormEditTopicPage(
                                listTopicViewModel = koinViewModel<ListTopicViewModel>(),
                                topicModel = data.topicModel,
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                },
                                onBackPressed = {
                                    navController.navigateUp()
                                }
                            )
                        }

                        composable<ListPostTestScreen>(
                            typeMap = mapOf(
                                typeOf<List<SectionModel>>() to CustomNavType.listSectionModel,
                                typeOf<List<PostTestModel>>() to CustomNavType.listPostTestModel
                            )
                        ) {
                            val data = it.toRoute<ListPostTestScreen>()
                            ListPostTestPage(
                                postTestViewModel = koinViewModel<PostTestViewModel>(),
                                trainingId = data.trainingId,
                                section = data.section,
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                },
                                navigateToAddPostTestPage = { section, trainingId, postTest ->
                                    navController.navigate(
                                        FormAddPostTestScreen(
                                            section = section,
                                            trainingId = trainingId,
                                            postModel = postTest
                                        )
                                    )
                                },
                                onBackPressed = {
                                    navController.navigateUp()
                                },
                                postTests = data.postTests
                            )
                        }

                        composable<FormAddPostTestScreen>(
                            typeMap = mapOf(
                                typeOf<SectionModel>() to CustomNavType.sectionModel,
                                typeOf<PostTestModel>() to CustomNavType.postTestModel,
                            )
                        ) {
                            val data = it.toRoute<FormAddPostTestScreen>()
                            FormAddPostTest(
                                postTestViewModel = koinViewModel<PostTestViewModel>(),
                                section = data.section,
                                trainingId = data.trainingId,
                                postTestModel = if (data.postModel.id != null) data.postModel else null
                            ) {
                                navController.navigateUp()
                            }
                        }

                        composable<ListTrainingKNScreen> {
                            ListTrainingKNPage(
                                trainingViewModel = koinViewModel<TrainingViewModel>(),
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                },
                                onBackPressed = {
                                    navController.navigateUp()
                                },
                                navigateToDetailTrainingKN = {
                                    navController.navigate(
                                        DetailTrainingKNScreen(
                                            trainingModel = it
                                        )
                                    )
                                }
                            )
                        }

                        composable<DetailTrainingKNScreen>(
                            typeMap = mapOf(
                                typeOf<TrainingModel>() to CustomNavType.trainingModel
                            )
                        ) {
                            val data = it.toRoute<DetailTrainingKNScreen>()
                            DetailTrainingKNPage(
                                karyaNyataViewModel = koinViewModel<KaryaNyataViewModel>(),
                                trainingModel = data.trainingModel,
                                onBackPressed = {
                                    navController.navigateUp()
                                },
                                navigateToDetailKaryaNyata = {
                                    navController.navigate(DetailKaryaNyataScreen(it))
                                }
                            ) {
                                navController.navigate(LoginScreen) {
                                    popUpTo(LoginScreen) {
                                        inclusive = false
                                    }
                                }
                            }
                        }

                        composable<DetailKaryaNyataScreen> {
                            val data = it.toRoute<DetailKaryaNyataScreen>()
                            DetailKaryaNyataPage(
                                karyaNyataId = data.kayaNyataId,
                                karyaNyataViewModel = koinViewModel<KaryaNyataViewModel>(),
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                },
                                onBackPress = {
                                    navController.navigateUp()
                                },
                            )
                        }

                        composable<ListUserAccountScreen> {
                            ListUserAccountPage(
                                userAccountViewModel = koinViewModel<UserAccountViewModel>(),
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                }
                            ) {
                                navController.navigate(RegisterScreen)
                            }
                        }

                        composable<ScannerScreen> {
                            when (PackageManager.PERMISSION_GRANTED) {
                                ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA) -> {
                                    ScannerPage(
                                        scannerViewModel = koinViewModel<ScannerViewModel>(),
                                        navigateToLoginPage = {
                                            navController.navigate(LoginScreen) {
                                                popUpTo(LoginScreen) {
                                                    inclusive = false
                                                }
                                            }
                                        },
                                    )
                                }
                                else -> {
                                    if (result) {
                                        ScannerPage(
                                            scannerViewModel = koinViewModel<ScannerViewModel>(),
                                            navigateToLoginPage = {
                                                navController.navigate(LoginScreen) {
                                                    popUpTo(LoginScreen) {
                                                        inclusive = false
                                                    }
                                                }
                                            },
                                        )
                                    } else {
                                        cameraPermission.launch(Manifest.permission.CAMERA)
                                    }
                                }
                            }
                        }

                        composable<Report> {
                            ReportPage(
                                reportViewModel = koinViewModel<ReportViewModel>(),
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                }
                            ) { userModel ->
                                navController.navigate(DetailReportScreen(userModel = userModel))
                            }
                        }

                        composable<DetailReportScreen>(
                            typeMap = mapOf(
                                typeOf<UserModel>() to CustomNavType.userModelType
                            )
                        ) {
                            val data = it.toRoute<DetailReportScreen>()
                            DetailReportPage(
                                userModel = data.userModel
                            )
                        }

                        composable<KabidListNewsScreen> {
                            KabidListNewsPage(
                                listNewsViewModel = koinViewModel<ListNewsViewModel>(),
                                navigateToFormEdit = {
                                    navController.navigate(EditNewsScreen(it))
                                },
                                onBackPressed = {
                                    navController.navigateUp()
                                }
                            ) {
                                navController.navigate(AddNewsScreen)
                            }
                        }

                        composable<AddNewsScreen> {
                            FormAddNewsPage(
                                newsViewModel = koinViewModel<NewsViewModel>(),
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                }
                            ) {
                                navController.navigateUp()
                            }
                        }

                        composable<EditNewsScreen>(
                            typeMap = mapOf(
                                typeOf<NewsModel>() to CustomNavType.newsModel
                            )
                        ) {
                            val data = it.toRoute<EditNewsScreen>().newsModel
                            FormEditNewsPage(
                                newsViewModel = koinViewModel<NewsViewModel>(),
                                newsModel = data,
                                navigateToLoginPage = {
                                    navController.navigate(LoginScreen) {
                                        popUpTo(LoginScreen) {
                                            inclusive = false
                                        }
                                    }
                                }
                            ) {
                                navController.navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }
}

