package com.maspam.etrain.training.core.presentation.utils

import com.maspam.etrain.training.domain.model.EnrollModel
import com.maspam.etrain.training.domain.model.NewsModel
import com.maspam.etrain.training.domain.model.PostTestModel
import com.maspam.etrain.training.domain.model.SectionModel
import com.maspam.etrain.training.domain.model.TopicModel
import com.maspam.etrain.training.domain.model.TrainingModel
import com.maspam.etrain.training.domain.model.UserModel
import kotlinx.serialization.Serializable


@Serializable
object InitialScreen

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
object ListTrainingScreen

@Serializable
object FormAddTrainingScreen

@Serializable
data class FormEditTrainingScreen(
    val trainingModel: TrainingModel
)

@Serializable
data class DetailTrainingScreen(
    val trainingModel: TrainingModel
)

/**
 * Section Management
 */
@Serializable
data class ListSectionScreen(
    val trainingId: Int,
    val section: List<SectionModel>
)

/**
 * Topic Management
 */
@Serializable
data class ListTopicScreen(
    val sectionId: Int,
    val topicList: List<TopicModel>,
)
@Serializable
data class FormUpdateTopicScreen(
    val topicModel: TopicModel,
)

/**
 * PostTest Management
 */
@Serializable
data class ListPostTestScreen(
    val trainingId: Int,
    val section: List<SectionModel>,
    val postTests: List<PostTestModel>
)
@Serializable
data class FormAddPostTestScreen(
    val section: SectionModel,
    val trainingId: Int,
    val postModel: PostTestModel,
)

/**
 * Karya Nyata Management
 */
@Serializable
object ListTrainingKNScreen

@Serializable
data class DetailTrainingKNScreen(
    val trainingModel: TrainingModel
)

@Serializable
data class DetailKaryaNyataScreen(
    val kayaNyataId: Int,
)


/**
 * Take Training Screen
 */
@Serializable
data class TakeTrainingScreen(
    val enrollModel: EnrollModel
)

@Serializable
object ListUserAccountScreen

@Serializable
object ScannerScreen


