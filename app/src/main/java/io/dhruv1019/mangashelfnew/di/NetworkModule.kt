package io.dhruv1019.mangashelfnewnewnew.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.dhruv1019.mangashelfnew.data.remote.ApiService
import io.dhruv1019.mangashelfnew.utils.Constants
import io.dhruv1019.mangashelfnew.data.local.DatabaseDao
import io.dhruv1019.mangashelfnew.data.local.ILocalDataSource
import io.dhruv1019.mangashelfnew.data.remote.IRemoteDataSource
import io.dhruv1019.mangashelfnew.data.local.LocalDatabase
import io.dhruv1019.mangashelfnew.data.MangaRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providesMovieApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesLocalDatabase(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(context, LocalDatabase::class.java, "local_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesTaskDao(db: LocalDatabase): DatabaseDao {
        return db.getDatabaseDao()
    }

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(Constants.BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun providesTaskRepository(
        remoteDataSource: IRemoteDataSource,
        localDataSource: ILocalDataSource
    ): MangaRepository {
        return MangaRepository(remoteDataSource,localDataSource)
    }
}