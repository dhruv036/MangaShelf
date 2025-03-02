package io.dhruv1019.mangashelfnew.data.remote

import android.util.Log
import io.dhruv1019.mangashelfnew.modal.Manga
import io.dhruv1019.mangashelfnew.data.BaseDataSource
import io.dhruv1019.mangashelfnew.utils.Result
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) : BaseDataSource(),
    IRemoteDataSource {

    override suspend fun fetchMangaList(): Result<List<Manga>> = getResult {
        Log.e("TAG", "fetchMangaList: called", )
        apiService.getMangaList()
    }

}

interface IRemoteDataSource {
    suspend fun fetchMangaList(): Result<List<Manga>>
}