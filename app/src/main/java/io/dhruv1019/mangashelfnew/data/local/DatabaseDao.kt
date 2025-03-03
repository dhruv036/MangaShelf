package io.dhruv1019.mangashelfnew.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.dhruv1019.mangashelfnew.modal.Manga

@Dao
interface DatabaseDao {

    @Query("SELECT * FROM Manga")
    fun getAllMangaList(): LiveData<List<Manga>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMangaList(mangaList : List<Manga>)

    @Query("SELECT * FROM Manga WHERE isFavourite = 1")
    fun fetchAllFavouriteManga(): LiveData<List<Manga>>

    @Query("SELECT EXISTS (SELECT 1 FROM Manga WHERE id = :mangaId AND isFavourite = 1)")
    fun isMangaFavorite(mangaId : String): LiveData<Boolean>

    @Query("UPDATE Manga SET isFavourite = :status WHERE id = :mangaId")
    suspend fun putMangaToFavourite(mangaId : String, status : Boolean)

    @Query("UPDATE Manga SET lastVisited = :lastVisited WHERE id = :mangaId")
    suspend fun putMangaLastVisited(mangaId : String, lastVisited : Long)

}