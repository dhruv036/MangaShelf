package io.dhruv1019.mangashelfnew.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.dhruv1019.mangashelfnew.data.MangaRepository
import io.dhruv1019.mangashelfnew.data.local.ILocalDataSource
import io.dhruv1019.mangashelfnew.data.remote.IRemoteDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providesTaskRepository(
        remoteDataSource: IRemoteDataSource,
        localDataSource: ILocalDataSource
    ): MangaRepository {
        return MangaRepository(remoteDataSource,localDataSource)
    }
}