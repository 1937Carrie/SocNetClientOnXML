package sdumchykov.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sdumchykov.data.repository.UsersRepositoryImpl
import sdumchykov.domain.repository.UsersRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    abstract fun bindUserRepository(
        usersRepository: UsersRepositoryImpl
    ): UsersRepository
}