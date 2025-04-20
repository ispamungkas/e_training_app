package com.maspam.etrain.training.core.presentation.utils

import com.maspam.etrain.training.domain.model.EnrollModel
import com.maspam.etrain.training.domain.model.NewsModel
import com.maspam.etrain.training.domain.model.TrainingModel
import com.maspam.etrain.training.domain.model.UserModel
import kotlinx.serialization.Serializable

@Serializable
object LoginScreen

@Serializable
object RegisterScreen

@Serializable
object ForgotPasswordScreen

@Serializable
data class OTPScreen(
    val nip: String
)

@Serializable
data class ChangePasswordScreen(
    val nip: String
)

/**
 * Dashboard Screen
 */

@Serializable
object TeacherDashboardScreen

@Serializable
object HeadSchoolDashboardScreen

@Serializable
object SuperUserDashboardScreen

@Serializable
object ListOpenTrainingScreen

@Serializable
object ListNewsScreen

@Serializable
data class DetailNewsScreen (
    val newsModel: NewsModel
)

/**
 * Profile Screen
 */

@Serializable
object ProfileScreen

@Serializable
object InformationScreen

@Serializable
object ChangePasswordProfileScreen

@Serializable
data class OTPRequestProfileScreen(
    val nip: String
)

@Serializable
object OTPValidateProfileScreen

@Serializable
data class EditProfileScreen(
    val userModel: UserModel
)

/**
 * Enroll Screen
 */

@Serializable
object ListEnrollTrainingProfileScreen

@Serializable
data class DetailEnrollTrainingScreen(
    val enrolModel: EnrollModel
)

/**
 * Training Screen
 */

@Serializable
data class DetailTrainingScreen(
    val trainingModel: TrainingModel
)

/**
 * Take Training Screen
 */
@Serializable
data class TakeTrainingScreen(
    val enrollModel: EnrollModel
)


