package io.dhruv1019.mangashelfnew

import androidx.lifecycle.LiveData
import javax.inject.Inject

class LocalDataSource  @Inject constructor(localDatabase: LocalDatabase) : ILocalDataSource {

    private val databaseDao = localDatabase.getDatabaseDao()

    override suspend fun addMangaList(mangaList: List<Manga>) =  databaseDao.addMangaList(mangaList)

    override fun getAllMangaList() = databaseDao.getAllMangaList()

    override fun getFavMangaList() = databaseDao.fetchAllFavouriteManga()

    override suspend fun putMangaFavourite(mangaId: String, status : Boolean) {
        databaseDao.putMangaToFavourite(mangaId, status)
    }


}


interface ILocalDataSource {
    suspend fun addMangaList(mangaList: List<Manga>)
    fun getAllMangaList() : LiveData<List<Manga>>
    fun getFavMangaList() : LiveData<List<Manga>>
    suspend fun putMangaFavourite(mangaId : String, status : Boolean)
}