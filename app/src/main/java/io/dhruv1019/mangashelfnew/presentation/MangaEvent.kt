package io.dhruv1019.mangashelfnew.presentation

import io.dhruv1019.mangashelfnew.Manga

sealed class MangaEvent {

    data class Favourite(val mangaId: String, val isFavourite: Boolean) : MangaEvent()

}