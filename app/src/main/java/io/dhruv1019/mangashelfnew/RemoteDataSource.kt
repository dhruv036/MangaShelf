package io.dhruv1019.mangashelfnew

import android.util.Log
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) : BaseDataSource(), IRemoteDataSource{

    override suspend fun fetchMangaList(): Result<List<Manga>> = getResult {
        Log.e("TAG", "fetchMangaList: called", )
        apiService.getMangaList()
    }

}

interface IRemoteDataSource {
    suspend fun fetchMangaList(): Result<List<Manga>>
}