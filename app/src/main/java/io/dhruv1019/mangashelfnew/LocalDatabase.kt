package io.dhruv1019.mangashelfnew

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Manga::class],
    version = 2,
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getDatabaseDao(): DatabaseDao
}
