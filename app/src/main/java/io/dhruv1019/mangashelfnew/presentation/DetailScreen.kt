package io.dhruv1019.mangashelfnew.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import io.dhruv1019.mangashelfnew.Constants
import io.dhruv1019.mangashelfnew.Manga
import io.dhruv1019.mangashelfnew.R
import io.dhruv1019.mangashelfnew.presentation.MangaEvent
import io.dhruv1019.mangashelfnew.presentation.giveMonthAndYear
import io.dhruv1019.mangashelfnew.ui.theme.backgroundColor
import io.dhruv1019.mangashelfnew.ui.theme.textColor
import kotlinx.coroutines.Dispatchers


@Preview
@Composable
fun DetailScreen(
    manga: Manga = Constants.dummyMangaList.first(),
    onBack: () -> Unit = {},
    onMangaEvent: (MangaEvent) -> Unit = {}
    ) {
    Scaffold { padding ->
        MangaItem2(
            manga = manga,
            modifier = Modifier.padding(top = padding.calculateTopPadding()),
            onBack = onBack,
            onFavouriteClick = {
                onMangaEvent(MangaEvent.Favourite(mangaId = manga.id, isFavourite = it))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MangaItem2(
    manga: Manga,
    onBack: () -> Unit = {},
    onFavouriteClick: (state: Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val listener = object : ImageRequest.Listener {
        override fun onError(request: ImageRequest, result: ErrorResult) {
            super.onError(request, result)
        }

        override fun onSuccess(request: ImageRequest, result: SuccessResult) {
            super.onSuccess(request, result)
        }
    }
    val rankWithIcon = when {
        manga.popularity > 90000 -> "\uD83E\uDD47 ${manga.popularity}"
        manga.popularity > 50000 -> "\uD83E\uDD48 ${manga.popularity}"
        else -> "\uD83E\uDD49 ${manga.popularity}"
    }

    val imageRequest = ImageRequest.Builder(context)
        .data(manga.image)
        .listener(listener)
        .dispatcher(Dispatchers.IO)
        .memoryCacheKey(manga.image)
        .diskCacheKey(manga.image)
        .placeholder(R.drawable.dummy_manga)
        .error(R.drawable.ic_launcher_background)
        .fallback(R.drawable.ic_launcher_background)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column {
            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f),
                contentScale = ContentScale.FillBounds
            )


            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
            ) {

                Text(
                    text = manga.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                    color = textColor,
                    lineHeight = TextUnit(24f, TextUnitType.Sp),
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Text(
                    text = manga.category,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
                    color = textColor,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)) {
                    Icon(
                        Icons.Rounded.Star,
                        tint = textColor,
                        contentDescription = "Score"
                    )
                    Text(
                        text = "  ".plus(manga.score.toString()),
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
                        color = textColor,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = rankWithIcon,
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
                        color = textColor
                    )

                    Text(
                        text = giveMonthAndYear(manga.publishedChapterDate.times(1000)), // To convert second into millisecond
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
                        color = textColor
                    )
                }


            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                onBack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Bookmark Icon",
                    tint = textColor,
                    modifier = Modifier
                        .height(64.dp)
                )
            }

            IconButton(
                onClick = {
                    onFavouriteClick(!manga.isFavourite)
                },
            ) {
                Icon(
                    imageVector = if (manga.isFavourite) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark Icon",
                    tint = textColor,
                    modifier = Modifier
                        .height(64.dp)
                )
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                        startY = 80f, // Center fade start
                        endY = Float.POSITIVE_INFINITY // Bottom fade end
                    )
                )
        )

    }
}