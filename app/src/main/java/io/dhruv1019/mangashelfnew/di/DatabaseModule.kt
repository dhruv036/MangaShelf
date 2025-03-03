package io.dhruv1019.mangashelfnew.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.dhruv1019.mangashelfnew.data.local.DatabaseDao
import io.dhruv1019.mangashelfnew.data.local.LocalDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

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

}