package io.dhruv1019.mangashelfnew.data

import androidx.lifecycle.LiveData
import io.dhruv1019.mangashelfnew.data.local.ILocalDataSource
import io.dhruv1019.mangashelfnew.data.remote.IRemoteDataSource
import io.dhruv1019.mangashelfnew.modal.Manga
import io.dhruv1019.mangashelfnew.utils.Result
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

    suspend fun putMangaLastVisited(mangaId : String, timestamp : Long){
        localDataSource.putMangaLastVisited(mangaId, timestamp)
    }

    suspend fun isMangaFavorite(mangaId : String) :LiveData<Boolean> = localDataSource.isMangaFavorite(mangaId)




}