package io.dhruv1019.mangashelfnew.utils

import io.dhruv1019.mangashelfnew.modal.Manga
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.Locale

object Constants {
    const val BASE_URL = "https://www.jsonkeeper.com/"

    val dummyMangaList = listOf(
        Manga(
            id = "4e70e91ac092255ef70016d6",
            image = "https://www.ubuy.co.in/productimg/?image=aHR0cHM6Ly9pbWFnZXMtY2RuLnVidXkuY28uaW4vNjMzZmViOGJkMjc5MTYzNDc2Mzc0YWQxLWphcGFuLWFuaW1lLW1hbmdhLXBvc3Rlci1qdWp1dHN1LmpwZw.jpg",
            score = 16.5,
            popularity = 165588L,
            title = "Neon Genesis Evangelion: Shinji Ikari Raising Project",
            publishedChapterDate = 1275542373,
            category = "Mystery",
            isFavourite = false,
            lastVisited = 1711459200123
        ),
        Manga(
            id = "4e70e91ac092255ef70016c2",
            image = "https://www.ubuy.co.in/productimg/?image=aHR0cHM6Ly9pbWFnZXMtY2RuLnVidXkuY28uaW4vNjMzZmViOGJkMjc5MTYzNDc2Mzc0YWQxLWphcGFuLWFuaW1lLW1hbmdhLXBvc3Rlci1qdWp1dHN1LmpwZw.jpg",
            score = 54.5,
            popularity = 54563,
            title = "The Legend of Zelda - Wind Waker - Links Log Book",
            publishedChapterDate = 1211400000,
            category = "Adventure",
            isFavourite = false,
            lastVisited = 1711459200123
        )
    )

    fun getFormattedDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp)) // Multiply by 1000 to convert seconds to milliseconds
    }

    fun getYearFromUnixTime(unixTime: Long): Int {
        return Instant.ofEpochMilli(unixTime.times(1000))  // Convert into milliseconds
            .atZone(ZoneId.systemDefault())
            .year
    }

    fun giveMonthAndYear(timestamp: Long): String {
        return SimpleDateFormat("MMMM, yyyy", Locale.getDefault()).format(timestamp)
    }

}