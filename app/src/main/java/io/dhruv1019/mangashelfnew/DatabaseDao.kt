package io.dhruv1019.mangashelfnew

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DatabaseDao {

    @Query("SELECT * FROM Manga")
    fun getAllMangaList(): LiveData<List<Manga>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMangaList(mangaList : List<Manga>)

    @Query("SELECT * FROM Manga WHERE isFavourite = 1")
    fun fetchAllFavouriteManga(): LiveData<List<Manga>>

    @Query("UPDATE Manga SET isFavourite = :status WHERE id = :mangaId")
    suspend fun putMangaToFavourite(mangaId : String, status : Boolean)

}