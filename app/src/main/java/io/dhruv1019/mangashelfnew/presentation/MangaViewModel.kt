package io.dhruv1019.mangashelfnew.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dhruv1019.mangashelfnew.Manga
import io.dhruv1019.mangashelfnew.Result
import io.dhruv1019.mangashelfnew.data.MangaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import javax.annotation.meta.When
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(val repository: MangaRepository) : ViewModel(){

    private val _yearIndexMap = MutableStateFlow(mutableMapOf<Int, Int>())
    val yearIndexMap: StateFlow<Map<Int, Int>> = _yearIndexMap

    val mangaList: StateFlow<Result<List<Manga>>> = repository.mangaList
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
                        result.data?.sortedBy { getYearFromUnixTime(it.publishedChapterDate) } ?: emptyList()
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

    val favouriteMangaList: StateFlow<List<Manga>> = repository.favMangaList
        .asFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, listOf<Manga>())

    private var _scrollMangaListToPosition = MutableStateFlow(0)
    val scrollMangaListToPosition: StateFlow<Int> = _scrollMangaListToPosition


    val yearList: StateFlow<List<Int>> = mangaList
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
        }
    }

    private fun getYearFromUnixTime(unixTime: Long): Int {
        return Instant.ofEpochMilli(unixTime.times(1000))  // Convert into milliseconds
            .atZone(ZoneId.systemDefault())
            .year
    }
}