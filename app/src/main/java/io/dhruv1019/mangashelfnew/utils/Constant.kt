package io.dhruv1019.mangashelfnew.utils

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
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

    object Utils {
        fun lerp(start: Float, stop: Float, fraction: Float): Float =
            (1 - fraction) * start + fraction * stop


        val EaseOutBounce: Easing = Easing { fraction ->
            val n1 = 7.5625f
            val d1 = 2.75f
            var newFraction = fraction

            return@Easing if (newFraction < 1f / d1) {
                n1 * newFraction * newFraction
            } else if (newFraction < 2f / d1) {
                newFraction -= 1.5f / d1
                n1 * newFraction * newFraction + 0.75f
            } else if (newFraction < 2.5f / d1) {
                newFraction -= 2.25f / d1
                n1 * newFraction * newFraction + 0.9375f
            } else {
                newFraction -= 2.625f / d1
                n1 * newFraction * newFraction + 0.984375f
            }
        }
        val EaseOutQuart = CubicBezierEasing(0.25f, 1f, 0.5f, 1f)
    }

}