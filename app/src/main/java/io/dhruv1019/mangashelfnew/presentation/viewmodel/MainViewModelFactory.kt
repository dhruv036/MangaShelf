package io.dhruv1019.mangashelfnew.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dhruv1019.mangashelfnew.data.MangaRepository

class MangaViewModelFactory (private val repository: MangaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MangaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MangaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}