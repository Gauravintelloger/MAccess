package technology.dubaileading.maccessemployee.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.ui.attendance.AttendanceRepository
import technology.dubaileading.maccessemployee.ui.check_in.ActionRepository
import technology.dubaileading.maccessemployee.ui.home.HomeRepository
import technology.dubaileading.maccessemployee.ui.login.LoginRepository
import technology.dubaileading.maccessemployee.ui.notifications.NotificationsRepository
import technology.dubaileading.maccessemployee.ui.profile.ProfileRepository
import technology.dubaileading.maccessemployee.utility.NetworkHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesLoginRepository(
        retrofit: EmployeeEndpoint, networkHelper: NetworkHelper
    ): LoginRepository = LoginRepository(retrofit, networkHelper)

    @Provides
    @Singleton
    fun providesAttendanceRepository(
        retrofit: EmployeeEndpoint, networkHelper: NetworkHelper
    ): AttendanceRepository = AttendanceRepository(retrofit, networkHelper)

    @Provides
    @Singleton
    fun providesActionRepository(
        retrofit: EmployeeEndpoint, networkHelper: NetworkHelper
    ): ActionRepository = ActionRepository(retrofit, networkHelper)

    @Provides
    @Singleton
    fun providesHomeRepository(
        retrofit: EmployeeEndpoint, networkHelper: NetworkHelper
    ): HomeRepository = HomeRepository(retrofit, networkHelper)

    @Provides
    @Singleton
    fun providesNotificationRepository(
        retrofit: EmployeeEndpoint, networkHelper: NetworkHelper
    ): NotificationsRepository = NotificationsRepository(retrofit, networkHelper)

    @Provides
    @Singleton
    fun providesProfileRepository(
        retrofit: EmployeeEndpoint, networkHelper: NetworkHelper
    ): ProfileRepository = ProfileRepository(retrofit, networkHelper)

}