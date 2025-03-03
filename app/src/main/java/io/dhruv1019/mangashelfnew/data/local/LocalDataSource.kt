package io.dhruv1019.mangashelfnew.data.local

import androidx.lifecycle.LiveData
import io.dhruv1019.mangashelfnew.modal.Manga
import javax.inject.Inject

class LocalDataSource  @Inject constructor(localDatabase: LocalDatabase) : ILocalDataSource {

    private val databaseDao = localDatabase.getDatabaseDao()

    override suspend fun addMangaList(mangaList: List<Manga>) =  databaseDao.addMangaList(mangaList)

    override fun getAllMangaList() = databaseDao.getAllMangaList()

    override fun getFavMangaList() = databaseDao.fetchAllFavouriteManga()
    override fun isMangaFavorite(mangaId : String) =  databaseDao.isMangaFavorite(mangaId)

    override suspend fun putMangaFavourite(mangaId: String, status : Boolean) {
        databaseDao.putMangaToFavourite(mangaId, status)
    }

    override suspend fun putMangaLastVisited(mangaId: String, LastVisited: Long) {
        databaseDao.putMangaLastVisited(mangaId, LastVisited)
    }


}


interface ILocalDataSource {
    suspend fun addMangaList(mangaList: List<Manga>)
    fun getAllMangaList() : LiveData<List<Manga>>
    fun getFavMangaList() : LiveData<List<Manga>>
    fun isMangaFavorite(mangaId : String) : LiveData<Boolean>
    suspend fun putMangaFavourite(mangaId : String, status : Boolean)
    suspend fun putMangaLastVisited(mangaId : String, LastVisited : Long)
}