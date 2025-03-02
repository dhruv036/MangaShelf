package io.dhruv1019.mangashelfnewnew.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.dhruv1019.mangashelfnew.ILocalDataSource
import io.dhruv1019.mangashelfnew.IRemoteDataSource
import io.dhruv1019.mangashelfnew.LocalDataSource
import io.dhruv1019.mangashelfnew.RemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfaceModule {

    @Binds
    abstract fun provideRemoteDataSource(remoteDataSource: RemoteDataSource):
            IRemoteDataSource

    @Binds
    abstract fun provideLocalDataSource(localDataSource: LocalDataSource):
            ILocalDataSource
}