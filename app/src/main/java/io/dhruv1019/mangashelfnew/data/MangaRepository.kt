package io.dhruv1019.mangashelfnew.data

import androidx.lifecycle.LiveData
import io.dhruv1019.mangashelfnew.ILocalDataSource
import io.dhruv1019.mangashelfnew.IRemoteDataSource
import io.dhruv1019.mangashelfnew.Manga
import io.dhruv1019.mangashelfnew.Result
import io.dhruv1019.mangashelfnew.resultLiveData
import javax.inject.Inject

class MangaRepository @Inject constructor(
    private val remoteDataSource: IRemoteDataSource,
    private val localDataSource: ILocalDataSource
) {
    val mangaList: LiveData<Result<List<Manga>>> = resultLiveData(
        databaseQuery = { localDataSource.getAllMangaList() },
        networkCall = { remoteDataSource.fetchMangaList() },
        saveCallResult = {
            localDataSource.addMangaList(it)
        }
    )

    val favMangaList: LiveData<List<Manga>> = localDataSource.getFavMangaList()

    suspend fun putMangaFavourite(mangaId : String, status : Boolean){
        localDataSource.putMangaFavourite(mangaId, status)
    }



}