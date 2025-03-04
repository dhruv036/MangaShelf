package io.dhruv1019.mangashelfnew

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Manga")
data class Manga(
    @PrimaryKey
    val id: String,
    val image: String,
    val score: Double,
    val popularity: Long,
    val title: String,
    val publishedChapterDate: Long,
    val category: String,
    val isFavourite: Boolean = false
): Parcelable