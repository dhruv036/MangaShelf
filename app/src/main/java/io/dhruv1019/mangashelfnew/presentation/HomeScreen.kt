package io.dhruv1019.mangashelfnew.presentation

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import io.dhruv1019.mangashelfnew.R
import io.dhruv1019.mangashelfnew.R.font.gang_of_three
import io.dhruv1019.mangashelfnew.modal.Manga
import io.dhruv1019.mangashelfnew.modal.SortBy
import io.dhruv1019.mangashelfnew.ui.theme.backgroundColor
import io.dhruv1019.mangashelfnew.ui.theme.bottoSheetBackgroundColor
import io.dhruv1019.mangashelfnew.ui.theme.textColor
import io.dhruv1019.mangashelfnew.utils.Constants
import io.dhruv1019.mangashelfnew.utils.Constants.giveMonthAndYear
import io.dhruv1019.mangashelfnew.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs


@Composable
fun HomeScreen(
    mangaList: State<Result<List<Manga>>>,
    onMangaEvent: (MangaEvent) -> Unit = {},
    onNavigateToMangaDetail: (mangaID: String) -> Unit = {},
    yearList: State<Map<Int, Int>>,
    sortType: State<SortBy>,
    favmangaList: State<List<Manga>>
) {

    when (mangaList.value.status) {
        Result.Status.SUCCESS -> {
            val data = mangaList.value.data
            if (!data.isNullOrEmpty()) {
                MangaList(
                    mangaList = data,
                    onClick = {
                        onNavigateToMangaDetail(it)
                    },
                    onMangaEvent = onMangaEvent,
                    yearList = yearList.value,
                    sortType = sortType.value,
                    favmangaList = favmangaList.value
                )
            } else {
                EmptyState()
            }
        }

        Result.Status.ERROR -> {
            ErrorState(message = mangaList.value.message ?: "Something went wrong")
        }

        Result.Status.LOADING -> {
            LoadingState()
        }
    }
}

//@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MangaList(
    mangaList: List<Manga> = Constants.dummyMangaList,
    onClick: (mangaId: String) -> Unit = {},
    onMangaEvent: (MangaEvent) -> Unit = {},
    yearList: Map<Int, Int>,
    sortType: SortBy,
    favmangaList: List<Manga>,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val curSelectedYear = rememberSaveable {
        mutableStateOf(yearList.keys.first())
    }
    val pagerState =
        rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f, pageCount = { 0 })
    val bottomSheetState = rememberModalBottomSheetState()
    val isBottomSheetOpened = rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = curSelectedYear.value) {
//        Log.e("TAG", "MangaList: ${curSelectedYear.value}")
        yearList[curSelectedYear.value]?.let { listState.scrollToItem(it) }
    }
    LaunchedEffect(key1 = listState.firstVisibleItemIndex) {
        val firstVisibleIndex = listState.firstVisibleItemIndex

        val matchingYear = yearList
            .entries
            .minByOrNull { (_, index) -> if (index <= firstVisibleIndex) firstVisibleIndex - index else Int.MAX_VALUE }
            ?.key

        matchingYear?.let {
            if (curSelectedYear.value != it) {
                curSelectedYear.value = it
            }
        }

    }


    Scaffold(
        modifier = Modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(padding)
        ) {

            Text(
                text = stringResource(R.string.manga_shelf),
                fontFamily = FontFamily(Font(gang_of_three)),
                fontSize = 30.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            if (favmangaList.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.favourite_manga),
                    fontFamily = FontFamily(Font(gang_of_three)),
                    fontSize = 24.sp,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(600),
                    modifier = Modifier
                        .padding(16.dp)
                )

                LazyRow(horizontalArrangement = Arrangement.Center) {
                    itemsIndexed(favmangaList) { index, manga ->
                        val pageOffset = calculateOffsetForItem(index, listState)
                        FavouriteMangaItem(manga = manga, pageOffset = pageOffset, navigateToDetail = {
                            onClick(manga.id)
                        })
                    }
                }
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "All Manga",
                    fontFamily = FontFamily(Font(gang_of_three)),
                    fontSize = 24.sp,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(600),
                    modifier = Modifier
                        .padding(16.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        isBottomSheetOpened.value = !isBottomSheetOpened.value

                    }) {
                    Text(
                        text = sortType.sortname,
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
                        color = textColor,
                        modifier = Modifier
                            .padding(end = 16.dp)
                    )
                    IconButton(onClick = {
                        isBottomSheetOpened.value = !isBottomSheetOpened.value
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "Sorting Icon",
                            tint = textColor,
                            modifier = Modifier
                                .height(64.dp)
                        )
                    }
                }

            }

            if (sortType == SortBy.NONE) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.year),
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
                        color = textColor,
                        modifier = Modifier
                            .padding(end = 16.dp)
                    )
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        items(yearList.keys.toList(), key = { it }) {
                            TextButton(onClick = {
                                yearList[it]?.let { index ->
                                    coroutineScope.launch {
                                        coroutineScope.launch {
                                            listState.scrollToItem(index)
                                        }
                                    }
                                }
                            }) {
                                Text(
                                    text = it.toString(),
                                    fontFamily = FontFamily(Font(R.font.montserrat)),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = textColor,
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(color = if (curSelectedYear.value == it) Color.Gray else Color.Transparent)
                                        .border(
                                            1.dp,
                                            if (curSelectedYear.value == it) Color.Transparent else Color.Gray,
                                            RoundedCornerShape(16.dp)
                                        )
                                        .padding(vertical = 8.dp, horizontal = 16.dp)

                                )
                            }

                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier,
                state = listState
            ) {
                items(mangaList, key = { it.id }) { manga ->
                    MangaItem(
                        manga = manga,
                        navigateToDetail = {
                            onClick(manga.id)
                        },
                        onMangaEvent = {
                            onMangaEvent(it)
                        },
                        isFavouriteMarked = favmangaList.contains(manga)
                    )
                }
            }

        }

        if (isBottomSheetOpened.value) {
            ModalBottomSheet(
                onDismissRequest = {
                    isBottomSheetOpened.value = false
                },
                sheetState = bottomSheetState,
                containerColor = bottoSheetBackgroundColor
            ) {
                LazyColumn {
                    items(SortBy.entries, key = { it.name }) {
                        TextButton(
                            onClick = {
                                onMangaEvent(MangaEvent.Sort(it))
                                isBottomSheetOpened.value = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (sortType == it) Color(0xFFE0E0E0) else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (sortType == it) textColor else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = it.sortdsc,
                                fontFamily = FontFamily(Font(R.font.montserrat)),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = if (sortType == it) bottoSheetBackgroundColor else textColor,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaItem(
    manga: Manga,
    navigateToDetail: () -> Unit = {},
    onMangaEvent: (MangaEvent) -> Unit = {},
    isFavouriteMarked: Boolean
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
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.drawable.ic_launcher_foreground)
        .fallback(R.drawable.ic_launcher_foreground)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()

    Card(
        onClick = {
            navigateToDetail()
        },
        modifier = Modifier.padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row {
            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                modifier = Modifier
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .aspectRatio(3f / 4f),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.height(180.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = manga.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        modifier = Modifier
                            .weight(1f),
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                        color = textColor,
                        lineHeight = TextUnit(24f, TextUnitType.Sp)
                    )
                    IconButton(
                        onClick = {
                            onMangaEvent(MangaEvent.Favourite(manga.id, !isFavouriteMarked))
                        },
                    ) {
                        Icon(
                            imageVector = if (isFavouriteMarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark Icon",
                            tint = textColor,
                            modifier = Modifier
                                .height(32.dp)
                        )
                    }
                }

                Text(
                    text = manga.category,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                    color = textColor
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.Star,
                        tint = textColor,
                        contentDescription = "Score"
                    )
                    Text(
                        text = "  ".plus(manga.score.toString()),
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                        color = textColor,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = rankWithIcon,
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                        color = textColor
                    )

                    Text(
                        text = giveMonthAndYear(manga.publishedChapterDate.times(1000)), // To convert second into millisecond
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                        color = textColor
                    )
                }
            }
        }
    }

}


@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "No Manga Available", fontSize = 18.sp, color = Color.Gray)
    }
}

@Composable
fun ErrorState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = Color.Red, fontSize = 18.sp)
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "LOADING ...")
    }
}

@Composable
fun calculateOffsetForItem(index: Int, listState: LazyListState): Float {
    val layoutInfo = listState.layoutInfo
    val visibleItems = layoutInfo.visibleItemsInfo

    val itemInfo = visibleItems.find { it.index == index } ?: return 0f
    val center = layoutInfo.viewportEndOffset / 2

    val itemCenter = (itemInfo.offset + itemInfo.size / 2)
    val distance = abs(itemCenter - center).toFloat()

    return distance / center
}


@Composable
fun FavouriteMangaItem(
    manga: Manga,
    pageOffset: Float,
    navigateToDetail: () -> Unit = {},
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
    val scaleXBox = Constants.Utils.lerp(
        start = 0.9f,
        stop = 1f,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
    val scaleYBox = Constants.Utils.lerp(
        start = 0.7f,
        stop = 1f,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
    val rotateY = Constants.Utils.lerp(
        start = 10f,
        stop = 0f,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
    val boxAngle: Float by animateFloatAsState(
        targetValue = rotateY,
        animationSpec = tween(durationMillis = 600, easing = Constants.Utils.EaseOutQuart)
    )
    val boxScaleX: Float by animateFloatAsState(
        targetValue = scaleXBox,

        animationSpec = tween(durationMillis = 800, easing = Constants.Utils.EaseOutQuart)
    )
    val boxScaleY: Float by animateFloatAsState(
        targetValue = scaleYBox,

        animationSpec = tween(durationMillis = 800, easing = Constants.Utils.EaseOutQuart)
    )
    val imageRequest = ImageRequest.Builder(context)
        .data(manga.image)
        .listener(listener)
        .dispatcher(Dispatchers.IO)
        .memoryCacheKey(manga.image)
        .diskCacheKey(manga.image)
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.drawable.ic_launcher_foreground)
        .fallback(R.drawable.ic_launcher_foreground)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()

    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            navigateToDetail()
        }) {
        AsyncImage(
            model = imageRequest,
            contentDescription = null,
            modifier = Modifier

                .padding(horizontal = 32.dp)
                .clip(RoundedCornerShape(16.dp))
                .height(160.dp)
                .aspectRatio(3f / 4f)
                .graphicsLayer {
                    Constants.Utils
                        .lerp(
                            start = 0.90f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                        .also {
                            scaleX = boxScaleX
                            scaleY = boxScaleY
                            rotationY = boxAngle
                        }
                },
            contentScale = ContentScale.FillBounds
        )
    }
}