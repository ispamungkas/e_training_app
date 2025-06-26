package com.maspam.etrain.training.di

import com.maspam.etrain.training.core.networking.HttpClientFactory
import com.maspam.etrain.training.core.presentation.utils.FileReader
import com.maspam.etrain.training.data.remote_datasource.local.proto.RemoteUserSessionDataSource
import com.maspam.etrain.training.data.remote_datasource.network.RemoteAuthenticationDataSource
import com.maspam.etrain.training.data.remote_datasource.network.RemoteEnrollDataSource
import com.maspam.etrain.training.data.remote_datasource.network.RemoteKaryaNyataDataSource
import com.maspam.etrain.training.data.remote_datasource.network.RemoteNewsDataSource
import com.maspam.etrain.training.data.remote_datasource.network.RemotePostTestDataSource
import com.maspam.etrain.training.data.remote_datasource.network.RemoteTrainingDataSource
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.AuthenticationDataSource
import com.maspam.etrain.training.domain.datasource.network.EnrollDataSource
import com.maspam.etrain.training.domain.datasource.network.KaryaNyataDataSource
import com.maspam.etrain.training.domain.datasource.network.NewsDataSource
import com.maspam.etrain.training.domain.datasource.network.PostTestDataSource
import com.maspam.etrain.training.domain.datasource.network.TrainingDataSource
import com.maspam.etrain.training.presentation.authentication.viewmodel.AuthenticationViewModel
import com.maspam.etrain.training.presentation.authentication.viewmodel.ChangePasswordViewModel
import com.maspam.etrain.training.presentation.authentication.viewmodel.ForgotPasswordViewModel
import com.maspam.etrain.training.presentation.authentication.viewmodel.OTPViewModel
import com.maspam.etrain.training.presentation.dashboard.viewmodel.DashboardViewModel
import com.maspam.etrain.training.presentation.dashboard.viewmodel.ListOpenTrainingViewModel
import com.maspam.etrain.training.presentation.dashboard.viewmodel.UserAccountViewModel
import com.maspam.etrain.training.presentation.enroll.viewmodel.EnrollViewModel
import com.maspam.etrain.training.presentation.enroll.viewmodel.ListEnrollProfileViewModel
import com.maspam.etrain.training.presentation.karyanyata.viewmodel.KaryaNyataViewModel
import com.maspam.etrain.training.presentation.news.viewmodel.ListNewsViewModel
import com.maspam.etrain.training.presentation.news.viewmodel.NewsViewModel
import com.maspam.etrain.training.presentation.profile.viewmodel.ProfileViewModel
import com.maspam.etrain.training.presentation.profile.viewmodel.UpdateDataProfileViewModel
import com.maspam.etrain.training.presentation.profile.viewmodel.UpdatePasswordProfileViewModel
import com.maspam.etrain.training.presentation.report.ReportViewModel
import com.maspam.etrain.training.presentation.scanner.ScannerViewModel
import com.maspam.etrain.training.presentation.taketraining.viewmodel.TakeTrainingViewModel
import com.maspam.etrain.training.presentation.training.viewmodel.DetailTrainingViewModel
import com.maspam.etrain.training.presentation.training.viewmodel.ListSectionViewModel
import com.maspam.etrain.training.presentation.training.viewmodel.ListTopicViewModel
import com.maspam.etrain.training.presentation.training.viewmodel.PostTestViewModel
import com.maspam.etrain.training.presentation.training.viewmodel.TrainingViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    single { androidApplication() }
    single { RemoteUserSessionDataSource(get()) }
    single { FileReader(context = get()) }

    singleOf(::RemoteUserSessionDataSource).bind<UserSessionDataSource>()
    singleOf(::RemoteAuthenticationDataSource).bind<AuthenticationDataSource>()
    singleOf(::RemoteTrainingDataSource).bind<TrainingDataSource>()
    singleOf(::RemoteNewsDataSource).bind<NewsDataSource>()
    singleOf(::RemoteEnrollDataSource).bind<EnrollDataSource>()
    singleOf(::RemoteKaryaNyataDataSource).bind<KaryaNyataDataSource>()
    singleOf(::RemotePostTestDataSource).bind<PostTestDataSource>()

    viewModelOf(::OTPViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::ChangePasswordViewModel)
    viewModelOf(::AuthenticationViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::ListOpenTrainingViewModel)
    viewModelOf(::UpdateDataProfileViewModel)
    viewModelOf(::UpdatePasswordProfileViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ListEnrollProfileViewModel)
    viewModelOf(::EnrollViewModel)
    viewModelOf(::TrainingViewModel)
    viewModelOf(::DetailTrainingViewModel)
    viewModelOf(::ListSectionViewModel)
    viewModelOf(::ListTopicViewModel)
    viewModelOf(::PostTestViewModel)
    viewModelOf(::ListNewsViewModel)
    viewModelOf(::TakeTrainingViewModel)
    viewModelOf(::KaryaNyataViewModel)
    viewModelOf(::UserAccountViewModel)
    viewModelOf(::ScannerViewModel)
    viewModelOf(::ReportViewModel)
    viewModelOf(::NewsViewModel)
}