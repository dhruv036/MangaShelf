package io.dhruv1019.mangashelfnew.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.dhruv1019.mangashelfnew.modal.Manga

@Database(
    entities = [Manga::class],
    version = 2,
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getDatabaseDao(): DatabaseDao
}
