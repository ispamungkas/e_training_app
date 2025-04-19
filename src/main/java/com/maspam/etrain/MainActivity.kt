package com.maspam.etrain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.maspam.etrain.training.core.presentation.utils.ChangePasswordProfileScreen
import com.maspam.etrain.training.core.presentation.utils.ChangePasswordScreen
import com.maspam.etrain.training.core.presentation.utils.CustomNavType
import com.maspam.etrain.training.core.presentation.utils.DetailEnrollTrainingScreen
import com.maspam.etrain.training.core.presentation.utils.EditProfileScreen
import com.maspam.etrain.training.core.presentation.utils.ForgotPasswordScreen
import com.maspam.etrain.training.core.presentation.utils.HeadSchoolDashboardScreen
import com.maspam.etrain.training.core.presentation.utils.InformationScreen
import com.maspam.etrain.training.core.presentation.utils.ListEnrollTrainingProfileScreen
import com.maspam.etrain.training.core.presentation.utils.ListOpenTrainingScreen
import com.maspam.etrain.training.core.presentation.utils.LoginScreen
import com.maspam.etrain.training.core.presentation.utils.OTPRequestProfileScreen
import com.maspam.etrain.training.core.presentation.utils.OTPScreen
import com.maspam.etrain.training.core.presentation.utils.OTPValidateProfileScreen
import com.maspam.etrain.training.core.presentation.utils.ProfileScreen
import com.maspam.etrain.training.core.presentation.utils.RegisterScreen
import com.maspam.etrain.training.core.presentation.utils.SuperUserDashboardScreen
import com.maspam.etrain.training.core.presentation.utils.TeacherDashboardScreen
import com.maspam.etrain.training.domain.model.EnrollModel
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
import com.maspam.etrain.training.presentation.dashboard.SuperUserDashboardPage
import com.maspam.etrain.training.presentation.dashboard.TeacherDashboardPage
import com.maspam.etrain.training.presentation.dashboard.extended.ListOpenTrainingPage
import com.maspam.etrain.training.presentation.dashboard.viewmodel.DashboardViewModel
import com.maspam.etrain.training.presentation.dashboard.viewmodel.ListOpenTrainingViewModel
import com.maspam.etrain.training.presentation.enroll.DetailEnrollTraining
import com.maspam.etrain.training.presentation.enroll.ListEnrollTrainingProfilePage
import com.maspam.etrain.training.presentation.enroll.viewmodel.ListEnrollProfileViewModel
import com.maspam.etrain.training.presentation.profile.ChangeDataProfilePage
import com.maspam.etrain.training.presentation.profile.ChangePasswordProfilePage
import com.maspam.etrain.training.presentation.profile.InformationPage
import com.maspam.etrain.training.presentation.profile.ProfilePage
import com.maspam.etrain.training.presentation.profile.RequestOtpPage
import com.maspam.etrain.training.presentation.profile.VerificationOtpPage
import com.maspam.etrain.training.presentation.profile.viewmodel.ProfileViewModel
import com.maspam.etrain.training.presentation.profile.viewmodel.UpdateDataProfileViewModel
import com.maspam.etrain.training.presentation.profile.viewmodel.UpdatePasswordProfileViewModel
import com.maspam.etrain.ui.theme.ETrainingTheme
import org.koin.androidx.compose.koinViewModel
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
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
                       startDestination = TeacherDashboardScreen
                   ) {
                       composable<LoginScreen> {
                           LoginPage(
                               authenticationViewModel = koinViewModel<AuthenticationViewModel>(),
                               onNavigateToForgotPasswordScreen = {
                                   navController.navigate(
                                       ForgotPasswordScreen
                                   )
                               },
                               onLoginSuccess = { userModel ->
                                   if (userModel.nip == "superuser") {
                                       navController.navigate(SuperUserDashboardScreen) {
                                           popUpTo(LoginScreen) {
                                               inclusive = true
                                           }
                                       }
                                   } else if (userModel.isHead == true) {
                                       navController.navigate(HeadSchoolDashboardScreen) {
                                           popUpTo(LoginScreen) {
                                               inclusive = true
                                           }
                                       }
                                   } else {
                                       navController.navigate(TeacherDashboardScreen) {
                                           popUpTo(LoginScreen) {
                                               inclusive = true
                                           }
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
                               dashboardViewModel = koinViewModel<DashboardViewModel>()
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
                               }
                           )
                       }
                       composable<HeadSchoolDashboardScreen> {
                           HeadSchoolDashboardPage(
                               dashboardViewModel = koinViewModel<DashboardViewModel>()
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
                                       popUpTo(TeacherDashboardScreen) {
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

                       composable<EditProfileScreen> (
                           typeMap = mapOf(
                               typeOf<UserModel>() to CustomNavType.userModelType
                           )
                       ){
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
                               } ,
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
                               onBackPressed = {
                                   navController.navigateUp()
                               }
                           )
                       }
                   }
               }
            }
        }
    }
}

