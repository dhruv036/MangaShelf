package io.dhruv1019.mangashelfnew.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dhruv1019.mangashelfnew.utils.Constants.getYearFromUnixTime
import io.dhruv1019.mangashelfnew.modal.Manga
import io.dhruv1019.mangashelfnew.utils.Result
import io.dhruv1019.mangashelfnew.modal.SortBy
import io.dhruv1019.mangashelfnew.data.MangaRepository
import io.dhruv1019.mangashelfnew.presentation.MangaEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(val repository: MangaRepository) : ViewModel(){

    private val _yearIndexMap = MutableStateFlow(mutableMapOf<Int, Int>())
    val yearIndexMap: StateFlow<Map<Int, Int>> = _yearIndexMap

    private val _sortBy = MutableStateFlow(SortBy.NONE)
    val sortBy: StateFlow<SortBy> = _sortBy


    val _mangaList: StateFlow<Result<List<Manga>>> = repository.mangaList
        .asFlow()
        .map { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    val yearMap =  mutableMapOf<Int, Int>()
                    result.data
                        ?.sortedBy { getYearFromUnixTime(it.publishedChapterDate) }
                        ?.forEachIndexed { index, manga ->
                            val year = getYearFromUnixTime(manga.publishedChapterDate)
                            yearMap.putIfAbsent(year, index)
                        }
                    _yearIndexMap.value = yearMap

                    Result.success(
                        result.data
                            ?.sortedBy { getYearFromUnixTime(it.publishedChapterDate) }
                            ?: emptyList()
                    )
                }
                Result.Status.ERROR -> {
                    Result.error(result.message ?: "Unknown error", result.data)
                }
                Result.Status.LOADING -> {
                    Result.loading(result.data)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, Result.loading())


    val mangaList: StateFlow<Result<List<Manga>>> = _mangaList
        .combine(_sortBy) { result, sortBy ->
            result.data?.let { manga ->
                val sortedManga = when (sortBy) {
                    SortBy.SCORE_LOW_TO_HIGH -> {
                        manga.sortedBy { it.score }
                    }
                    SortBy.SCORE_HIGH_TO_LOW -> {
                        manga.sortedByDescending { it.score }
                    }
                    SortBy.POPULARITY_LOW_TO_HIGH -> {
                        manga.sortedBy { it.popularity }
                    }
                    SortBy.POPULARITY_HIGH_TO_LOW -> {
                        manga.sortedByDescending { it.popularity}
                    }
                    SortBy.NONE -> manga
                }
                Result.success(sortedManga)
            } ?: result
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, Result.loading())


    val favouriteMangaList: StateFlow<List<Manga>> = repository.favMangaList
        .asFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, listOf<Manga>())

    private var _scrollMangaListToPosition = MutableStateFlow(0)
    val scrollMangaListToPosition: StateFlow<Int> = _scrollMangaListToPosition


    val yearList: StateFlow<List<Int>> = _mangaList
        .map { result ->
            result.data?.map { getYearFromUnixTime(it.publishedChapterDate) }
                ?.distinct()
                ?.sorted()
                ?: emptyList()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun mangaEvent(mangaEvent : MangaEvent){

        when(mangaEvent) {
            is MangaEvent.Favourite -> {
                viewModelScope.launch {
                    repository.putMangaFavourite(mangaEvent.mangaId,mangaEvent.isFavourite)
                }
            }
            is MangaEvent.Sort -> {
                Log.e("TAG", "MangaEvent.Sort: ${mangaEvent.sortType}", )
                _sortBy.value = mangaEvent.sortType
            }

            is MangaEvent.Visited -> {
                viewModelScope.launch {
                    repository.putMangaLastVisited(mangaEvent.mangaId,mangaEvent.timestamp)
                }
            }
        }
    }


}