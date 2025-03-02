package io.dhruv1019.mangashelfnew.presentation

import io.dhruv1019.mangashelfnew.modal.SortBy

sealed class MangaEvent {

    data class Favourite(val mangaId: String, val isFavourite: Boolean) : MangaEvent()
    data class Sort(val sortType: SortBy) : MangaEvent()
    data class Visited(val mangaId: String, val timestamp: Long) : MangaEvent()

}